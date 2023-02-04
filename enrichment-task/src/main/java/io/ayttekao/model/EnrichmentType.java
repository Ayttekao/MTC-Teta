package io.ayttekao.model;

public enum EnrichmentType {
    MSISDN;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
