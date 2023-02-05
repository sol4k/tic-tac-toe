package org.sol4k.tictactoe;

import org.sol4k.*;

import java.util.List;
import java.util.Scanner;

public class Game {
    private Connection connection;
    private PublicKey programId;
    private PublicKey gameAccount;
    private Keypair playerKeypair;

    Game(Connection connection, PublicKey programId, PublicKey gameAccount, Keypair playerKeypair) {
        this.connection = connection;
        this.programId = programId;
        this.gameAccount = gameAccount;
        this.playerKeypair = playerKeypair;
    }

    void join() {
        var playerPublicKey = playerKeypair.getPublicKey();
        System.out.println("Player " + playerPublicKey + " joins game " + gameAccount);
        var latestBlockhash = connection.getLatestBlockhash();
        byte[] data = {107, 112, 18, 38, 56, -83, 60, -128};
        var accounts = List.of(
                AccountMeta.writable(gameAccount),
                AccountMeta.signerAndWritable(playerPublicKey)
        );
        var instruction = new BaseInstruction(data, accounts, programId);
        var joinGameTransaction = new Transaction(
                latestBlockhash.getBlockhash(),
                instruction,
                playerPublicKey
        );
        joinGameTransaction.sign(playerKeypair);
        var signature = connection.sendTransaction(joinGameTransaction);
        System.out.println("Player " + playerPublicKey + " joined the game, transaction signature: " + signature);
    }

    void play() {
        Scanner reader = new Scanner(System.in);
        var playerPublicKey = playerKeypair.getPublicKey();
        while (true) {
            try {
                System.out.println("Enter an index of the game field (0 to 8):");
                if (!reader.hasNext()) {
                    return;
                }
                byte index = reader.nextByte();
                System.out.println("Setting the value to " + index + " on the game filed...");
                byte[] instructionData = {-3, -42, 48, -55, 100, -55, -29, -37, index};
                var accounts = List.of(
                        AccountMeta.writable(gameAccount),
                        AccountMeta.signerAndWritable(playerPublicKey)
                );
                var joinGameInstruction = new BaseInstruction(instructionData, accounts, programId);
                var latestBlockhash = connection.getLatestBlockhash();
                var setValueTransaction = new Transaction(
                        latestBlockhash.getBlockhash(),
                        joinGameInstruction,
                        playerPublicKey
                );
                setValueTransaction.sign(playerKeypair);
                var signature = connection.sendTransaction(setValueTransaction);
                System.out.println("Player " + playerPublicKey + " has set a value, transaction signature: " + signature);
            } catch (Exception e) {
                System.out.println("Something went wrong....");
                e.printStackTrace();
            }
        }
    }
}
