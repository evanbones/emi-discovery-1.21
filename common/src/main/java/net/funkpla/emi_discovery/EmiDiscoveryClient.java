package net.funkpla.emi_discovery;

public class EmiDiscoveryClient {
        public static void joinWorld() {
            KnownItems.loadFromDisk();
        }

        public static void leaveWorld() {
            KnownItems.clear();
        }
}

