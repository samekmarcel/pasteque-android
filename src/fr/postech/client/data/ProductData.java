/*
    POS-Tech Android
    Copyright (C) 2012 SARL SCOP Scil (contact@scil.coop)

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
package fr.postech.client.data;

import fr.postech.client.models.Product;

import android.content.Context;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductData {

    private static final String FILENAME = "products.data";

    public static List<Product> products = new ArrayList<Product>();

    public static void setProducts(List<Product> p) {
        products = p;
    }

    public static boolean save(Context ctx)
        throws IOException {
        FileOutputStream fos = ctx.openFileOutput(FILENAME, ctx.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(products);
        oos.close();
        return true;
    }

    public static boolean load(Context ctx)
        throws IOException {
        boolean ok = false;
        FileInputStream fis = ctx.openFileInput(FILENAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            products = (List) ois.readObject();
            if (products.size() > 0) {
                ok = true;
            }
        } catch (ClassNotFoundException cnfe) {
            // Should never happen
        }
        ois.close();
        return ok;
    }
    
}