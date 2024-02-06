package es.informaticamovil.anr

class Hilo: Thread() {
    override fun run() {
            super.run();
            Thread.sleep(1000);
        }
    }
