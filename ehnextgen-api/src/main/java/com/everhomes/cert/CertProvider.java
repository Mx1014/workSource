package com.everhomes.cert;

public interface CertProvider {
    void createCert(Cert cert);
    Cert findCertByName(String name);
    void deleteCert(Cert cert);
}
