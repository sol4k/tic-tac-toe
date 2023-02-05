package org.sol4k.tictactoe;

import org.sol4k.Base58;
import org.sol4k.Connection;
import org.sol4k.Keypair;
import org.sol4k.PublicKey;

public class Main {
    public static void main(String[] args) {
        var programId = new PublicKey("4Y5g1y7JXb2hpHDWsGQTAhHKbSJtR95W6QGzoXgESN8U");
        var gameAccount = new PublicKey(args[0]);
        var connection = new Connection("https://api.devnet.solana.com");
        var playerKeypair = Keypair.fromSecretKey(Base58.decode(
                "2WGcYYau2gLu2DUq68SxxXQmCgi77n8hFqqLNbNyg6Xfh2m3tvg8LF5Lgh69CFDux41LUKV1ak1ERHUqiBZnyshz"
        ));

        var game = new Game(connection, programId, gameAccount, playerKeypair);
        game.join();
        game.play();
    }
}