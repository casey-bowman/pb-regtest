package io.pacificbay.regtest

import groovy.transform.Synchronized
import groovyx.net.http.HTTPBuilder
import org.apache.http.conn.HttpHostConnectException

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*


class BitcoinServer {

    public static final String ACCOUNT_ONE = "accountOne"
    public static final String ACCOUNT_TWO = "accountTwo"

    AtomicInteger id = new AtomicInteger()

    volatile boolean serverOn = false

    HTTPBuilder http = new HTTPBuilder( 'http://bitcoinrpc:rEsu5YyCzVVfwC1Te25W1RSHEs2hD9aTpimMiFD89sD@127.0.0.1:18332/' )

    ExecutorService executor = Executors.newSingleThreadExecutor()

    @Synchronized
    void start(){
        if (!serverOn) {
            executor.submit(new BitcoinServerTask())
            while (this.info == "") sleep 1000
            serverOn = true
        }
    }

    @Synchronized
    void stop() {
        if (serverOn) {
            post("stop", [])
            serverOn = false
        }
    }

    def post(String command, def params) {
        def result = ""
        try {
            http.request(POST, JSON) { req ->

                body = [
                        "jsonrpc": "1.0",
                        "method" : command,
                        "params" : params,
                        "id": id.incrementAndGet()
                ]

                response.success = { resp, json ->
                    result = json.result
                }

                response.failure = { resp ->
                }
            }
        }
        catch(HttpHostConnectException e) {
        }
        result
    }


    String getInfo(){
        post("getinfo", [])
    }

    double getBalance() {
        post("getbalance", [""])
    }

    double getBalance(String account) {
        post("getbalance", [account])
    }

    /*
    Note: The extra call to getnewaddress is a workaround for a bug.
    There is a new address used for the miner which is returned
    by a subsequent getnewaddress call. Best to have this address assigned to the "" account.
    If people use the "" account for testing, one might instead set up a miner account
    and use that here instead of "".

    I was calling getnewaddress on test accounts and finding that 100 blocks later
    these test accounts would have balances that would include block rewards.
     */
    def generateInitialBitcoins() {
        def blocks = post("setgenerate", [true, 101])
        post("generate", [101])
        post("getnewaddress", [""])
        blocks
    }

    def confirm() {
        def block = post("setgenerate", [true])
        post("generate", [1])
        post("getnewaddress", [""])
        block
    }

    String getNewAddress(String account) {
        post("getnewaddress", [account])
    }

    String sendToAddress(String address, double amount) {
        post("sendtoaddress", [address, amount])
    }

    String sendFrom(String account, String address, double amount) {
        post("sendfrom", [account, address, amount])
    }


}
