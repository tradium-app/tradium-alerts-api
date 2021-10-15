package com.tradiumapp.swingtradealerts.services.models;

import java.util.List;

public class TipranksDataResponse {
    public String companyName;
    public List<PtConsensus> ptConsensus;
    public CompanyData companyData;

    public static class PtConsensus{
        public float period;
        public float priceTarget;
    }

    public static class CompanyData {
        public String sector;
        public String industry;
    }
}
