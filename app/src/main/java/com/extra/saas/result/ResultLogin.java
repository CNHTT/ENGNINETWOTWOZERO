package com.extra.saas.result;

/**
 * Created by 戴尔 on 2017/11/15.
 */

public class ResultLogin extends Result {
    private Voucher  data;

    public static class Voucher {
        private String voucher;

        public String getVoucher() {
            return voucher;
        }

        public void setVoucher(String voucher) {
            this.voucher = voucher;
        }
    }

    public Voucher getData() {
        return data;
    }

    public void setData(Voucher data) {
        this.data = data;
    }
}
