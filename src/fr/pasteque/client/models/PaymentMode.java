/*
    Pasteque Android client
    Copyright (C) Pasteque contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fr.pasteque.client.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.pasteque.client.R;

public class PaymentMode implements Serializable {

    /** Must be assigned to a customer */
    public static final int CUST_ASSIGNED = 1;
    /** Is debt (includes CUST_ASSIGNED) */
    public static final int CUST_DEBT = 2 + CUST_ASSIGNED;
    public static final int CUST_PREPAID = 4 + CUST_ASSIGNED;

    private int id;
    private String code;
    private String label;
    private int flags;
    private boolean hasImage;
    private List<Rule> rules;
    private boolean active;
    private int dispOrder;

    public PaymentMode(int id, String code, String label, int flags,
            boolean hasImage, List<Rule> rules, boolean active, int dispOrder) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.flags = flags;
        this.hasImage = hasImage;
        if (rules == null) {
            this.rules = new ArrayList<Rule>();
        } else {
            this.rules = rules;
        }
        this.active = active;
        this.dispOrder = dispOrder;
    }

    public static PaymentMode fromJSON(JSONObject o) throws JSONException {
        int id = o.getInt("id");
        String code = o.getString("code");
        String label = o.getString("label");
        int flags = o.getInt("flags");
        boolean hasImage = o.getBoolean("hasImage");
        boolean active = o.getBoolean("active");
        int dispOrder = o.getInt("dispOrder");
        List<Rule> rules = new ArrayList<Rule>();
        JSONArray jsRules = o.getJSONArray("rules");
        for (int i = 0; i < jsRules.length(); i++) {
            rules.add(Rule.fromJSON(jsRules.getJSONObject(i)));
        }
        return new PaymentMode(id, code, label, flags, hasImage, rules, active,
                dispOrder);
    }

    public int getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getCode() {
        return this.code;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean hasImage() {
        return this.hasImage;
    }

    public boolean isDebt() {
        return (this.flags & CUST_DEBT) == CUST_DEBT;
    }
    public boolean isPrepaid() {
        return (this.flags & CUST_PREPAID) == CUST_PREPAID;
    }
    public boolean isCustAssigned() {
        return (this.flags & CUST_ASSIGNED) == CUST_ASSIGNED;
    }

    public List<PaymentMode.Rule> getRules() {
        return this.rules;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("code", this.code);
        return o;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof PaymentMode)
                && ((PaymentMode)o).code.equals(this.code);
    }
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }

    public static class Rule implements Serializable {

        // Rule constants, see Rule on server
        public static final String CREDIT_NOTE = "note";
        public static final String GIVE_BACK = "give_back";
        public static final String PREPAID = "prepaid";
        public static final String DEBT = "debt";

        private double minVal;
        private String rule;

        public Rule(double minVal, String rule) {
            this.minVal = minVal;
            this.rule = rule;
        }

        public static Rule fromJSON(JSONObject o) throws JSONException {
            double minVal = o.getDouble("minVal");
            String rule = o.getString("rule");
            return new Rule(minVal, rule);
        }

        public double getMinVal() {
            return this.minVal;
        }

        public String getRule() {
            return this.rule;
        }

        /** User friendly function to check rule */
        public boolean is(String code) {
            return this.rule.equals(code);
        }

        /** Check if the rule applies for a given exceedent */
        public boolean appliesFor(double exceedent) {
            return this.minVal <= exceedent;
        }
    }
}
