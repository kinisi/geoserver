package cc.kinisi.geo.data;

import cc.kinisi.geo.data.auto._KinisiDomainMap;

public class KinisiDomainMap extends _KinisiDomainMap {

    private static KinisiDomainMap instance;

    private KinisiDomainMap() {}

    public static KinisiDomainMap getInstance() {
        if(instance == null) {
            instance = new KinisiDomainMap();
        }

        return instance;
    }
}
