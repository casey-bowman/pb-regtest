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

    def AtomicInteger id = new AtomicInteger()

    volatile boolean serverOn = false

    def http = new HTTPBuilder( 'http://bitcoinrpc:rEsu5YyCzVVfwC1Te25W1RSHEs2hD9aTpimMiFD89sD@127.0.0.1:18332/' )

    ExecutorService executor = Executors.newSingleThreadExecutor()

    @Synchronized
    void start(){
        if (!serverOn) {
            executor.submit(new BitcoinServerTask())
            while (this.info == "") sleep 1000
            serverOn = true
        }
    }

    String getInfo(){
        post("getinfo", [])
    }

    @Synchronized
    void stop() {
        if (serverOn) {
            post("stop", [])
            serverOn = false
        }
    }

    String post(String command, def params) {
        String result = ""
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

    String getBalance() {
        post("getbalance", [""])
    }
}
