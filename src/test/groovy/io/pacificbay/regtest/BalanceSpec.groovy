package io.pacificbay.regtest

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import static io.pacificbay.regtest.BitcoinServer.*


@Stepwise
class BalanceSpec extends Specification {

    @Shared boolean stopServerInCleanup = true
    //@Shared boolean stopServerInCleanup = false

    @Shared BitcoinServer bitcoinServer;

    @Shared String addressOne
    @Shared String addressTwo

    @Shared String txnOne
    @Shared String txnTwo

    def setupSpec(){
        deleteRegtest()
        bitcoinServer = new BitcoinServer()
        bitcoinServer.start()
    }

    def deleteRegtest() {
        def dataDir = new File("data")
        def regtestDir = new File(dataDir, "regTest")
        if (regtestDir.exists()) {
            regtestDir.deleteDir()
        }
    }

    def cleanupSpec(){
        if (stopServerInCleanup) bitcoinServer.stop()
    }

    def "check if server running"() {
        when:
            String info = bitcoinServer.info

        then:
            println(info)
            info != ""
    }

    def "generate some bitcoins"() {
        when:
            bitcoinServer.generateInitialBitcoins()

        then:
            int balance0 = bitcoinServer.balance
            println("Balance0: " + balance0)
            balance0 == 50
    }

    def "create a new address in account one and move 30 BTC there"() {
        when:
            addressOne = bitcoinServer.getNewAddress(ACCOUNT_ONE)
            println("Address1: " + addressOne)
            txnOne = bitcoinServer.sendToAddress(addressOne, 30)
            println("Txn1: " + txnOne)
            bitcoinServer.confirm()

        then:
            double balance1 = bitcoinServer.getBalance(ACCOUNT_ONE)
            println("Balance1: " + balance1)
            balance1 == 30
    }

    def "create a new address in account two and move 50 BTC there"() {
        when:
        addressTwo = bitcoinServer.getNewAddress(ACCOUNT_TWO)
        println("Address2: " + addressTwo)
        txnTwo = bitcoinServer.sendToAddress(addressTwo, 50)
        println("Txn2: " + txnTwo)
        bitcoinServer.confirm()

        then:
        double balance2 = bitcoinServer.getBalance(ACCOUNT_TWO)
        println("Balance2: " + balance2)
        balance2 == 50
    }

    def "send 5 BTC from account one to address 2"() {
        when:
        String txn3 = bitcoinServer.sendFrom(ACCOUNT_ONE, addressTwo, 5)
        println("Txn3: " + txn3)
        bitcoinServer.confirm()

        then:
        double balance1 = bitcoinServer.getBalance(ACCOUNT_ONE)
        double balance2 = bitcoinServer.getBalance(ACCOUNT_TWO)
        println("Balance1: " + balance1)
        println("Balance2: " + balance2)
        balance2 == 55

    }

}
