package io.pacificbay.regtest

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import static io.pacificbay.regtest.BitcoinServer.*

@Stepwise
class BalanceSpec2 extends Specification {

    private final String WALLET_ADDRESS = ""

    @Shared BitcoinServer bitcoinServer;

    @Shared String walletAddress = WALLET_ADDRESS

    def setupSpec(){
        bitcoinServer = new BitcoinServer()
    }


//    def "check if server running"() {
//        when:
//        String info = bitcoinServer.info
//
//        then:
//        println(info)
//        info != ""
//    }

    def "move 10 BTC from account1 to wallet address "() {
        when:
        String txn = bitcoinServer.sendFrom(ACCOUNT_ONE, walletAddress, 10)
        println("Txn: " + txn)
        bitcoinServer.confirm()

        then:
        Thread.sleep(1000)
        double balance2 = bitcoinServer.getBalance(ACCOUNT_TWO)
        println("Balance2: " + balance2)
        balance2 == 65
    }
}
