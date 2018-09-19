package com.example.camnc.cryptologogame.Common;

import android.content.Context;

import com.example.camnc.cryptologogame.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by camnc on 08/04/2018.
 */

public class Common {
    public static char[] user_submit_answer;
    public static char[] suggest_source;
    public static String[] alphabet_character = {
      "a", "b", "c", "d", "e", "f", "g", "h", "i",
      "j", "k", "l", "m", "n", "o", "p", "q", "r",
      "s", "t", "u", "v", "w", "x", "y", "z"
    };
    // references to images
    public static Integer[] thumbs = {
            // Level 1 //
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.ethereum, R.drawable.bitcoincash,
            R.drawable.ark, R.drawable.antshares,
            R.drawable.nem, R.drawable.dogecoin,
            R.drawable.ethereumclassic, R.drawable.neo,
            R.drawable.omisego, R.drawable.requestnetwork,
            R.drawable.vechain, R.drawable.stellar,
            //Level 2
            R.drawable.monero, R.drawable.ripple,
            R.drawable.decred, R.drawable.eos,
            R.drawable.cardano, R.drawable.iota,
            R.drawable.qtum, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            // Level 3 //
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            R.drawable.ethereum, R.drawable.ark,
            // Level 4 //
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            // Level 5 //
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo
    };

    // references correct to images
    public static Integer[] thumbsCorrect = {
            // Level 1 //
            R.drawable.bitcoint, R.drawable.litecoint,
            R.drawable.ethereumt, R.drawable.bitcoincasht,
            R.drawable.arkt, R.drawable.antsharest,
            R.drawable.nemt, R.drawable.dogecoint,
            R.drawable.ethereumclassict, R.drawable.neot,
            R.drawable.omisegot, R.drawable.requestnetworkt,
            R.drawable.vechaint, R.drawable.stellart,
            // Level 2 //
            R.drawable.monerot, R.drawable.ripplet,
            R.drawable.decredt, R.drawable.eost,
            R.drawable.cardanot, R.drawable.iotat,
            R.drawable.qtumt, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            // Level 3 //
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.dogecoin, R.drawable.monero,
            R.drawable.ethereum, R.drawable.ark,
            // Level 4 //
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            R.drawable.bitcoin, R.drawable.litecoin,
            // Level 5 //
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo,
            R.drawable.antshares, R.drawable.neo
    };
}
