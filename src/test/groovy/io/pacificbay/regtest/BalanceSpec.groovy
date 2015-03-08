package io.pacificbay.regtest

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise


@Stepwise
class BalanceSpec extends Specification {

    @Shared
    BitcoinServer bitcoinServer;

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
        bitcoinServer.stop()
    }

    def "check if server running"() {
        when:
            String info = bitcoinServer.info

        then:
            println(info)
            info != ""
    }

}
