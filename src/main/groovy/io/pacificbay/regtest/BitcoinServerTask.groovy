package io.pacificbay.regtest

import java.util.concurrent.Callable


class BitcoinServerTask implements Callable<BitcoinServerResult> {


    @Override
    BitcoinServerResult call() throws Exception {

        def props = new Properties()
        new File("local.properties").withReader('UTF-8') {
            reader -> props.load(reader)
        }
        def config = new ConfigSlurper().parse(props)
        String btcPath =config.btc.path
        "${btcPath}/bitcoin-qt -server -regtest -datadir=data -debug &".execute()

        return new BitcoinServerResult();
    }
}
