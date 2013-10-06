package com.mcintyret.bffm.types;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public enum Nutrient {
    PROCNT(203, "g", "Protein", 2),
    FAT(204, "g", "Total lipid (fat)", 2),
    CHOCDF(205, "g", "Carbohydrate, by difference", 2),
    ASH(207, "g", "Ash", 2),
    ENERC_KCAL(208, "kcal", "Energy", 0),
    STARCH(209, "g", "Starch", 2),
    SUCS(210, "g", "Sucrose", 2),
    GLUS(211, "g", "Glucose (dextrose)", 2),
    FRUS(212, "g", "Fructose", 2),
    LACS(213, "g", "Lactose", 2),
    MALS(214, "g", "Maltose", 2),
    ALC(221, "g", "Alcohol, ethyl", 1),
    WATER(255, "g", "Water", 2),
    ADJUSTED_PROTEIN(257, "g", "Adjusted Protein", 2),
    CAFFN(262, "mg", "Caffeine", 0),
    THEBRN(263, "mg", "Theobromine", 0),
    ENERC_KJ(268, "kJ", "Energy", 0),
    SUGAR(269, "g", "Sugars, total", 2),
    GALS(287, "g", "Galactose", 2),
    FIBTG(291, "g", "Fiber, total dietary", 1),
    CA(301, "mg", "Calcium, Ca", 0),
    FE(303, "mg", "Iron, Fe", 2),
    MG(304, "mg", "Magnesium, Mg", 0),
    P(305, "mg", "Phosphorus, P", 0),
    K(306, "mg", "Potassium, K", 0),
    NA(307, "mg", "Sodium, Na", 0),
    ZN(309, "mg", "Zinc, Zn", 2),
    CU(312, "mg", "Copper, Cu", 3),
    FLD(313, "mcg", "Fluoride, F", 1),
    MN(315, "mg", "Manganese, Mn", 3),
    SE(317, "mcg", "Selenium, Se", 1),
    VITA_IU(318, "IU", "Vitamin A, IU", 0),
    RETOL(319, "mcg", "Retinol", 0),
    VITA_RAE(320, "mcg_RAE", "Vitamin A, RAE", 0),
    CARTB(321, "mcg", "Carotene, beta", 0),
    CARTA(322, "mcg", "Carotene, alpha", 0),
    TOCPHA(323, "mg", "Vitamin E (alpha-tocopherol)", 2),
    VITD(324, "IU", "Vitamin D", 0),
    ERGCAL(325, "mcg", "Vitamin D2 (ergocalciferol)", 1),
    CHOCAL(326, "mcg", "Vitamin D3 (cholecalciferol)", 1),
    VITD_ALL(328, "mcg", "Vitamin D (D2 + D3)", 1),
    CRYPX(334, "mcg", "Cryptoxanthin, beta", 0),
    LYCPN(337, "mcg", "Lycopene", 0),
    LUT_ZEA(338, "mcg", "Lutein + zeaxanthin", 0),
    TOCPHB(341, "mg", "Tocopherol, beta", 2),
    TOCPHG(342, "mg", "Tocopherol, gamma", 2),
    TOCPHD(343, "mg", "Tocopherol, delta", 2),
    VITC(401, "mg", "Vitamin C, total ascorbic acid", 1),
    THIA(404, "mg", "Thiamin", 3),
    RIBF(405, "mg", "Riboflavin", 3),
    NIA(406, "mg", "Niacin", 3),
    PANTAC(410, "mg", "Pantothenic acid", 3),
    VITB6A(415, "mg", "Vitamin B-6", 3),
    FOL(417, "mcg", "Folate, total", 0),
    VITB12(418, "mcg", "Vitamin B-12", 2),
    CHOLN(421, "mg", "Choline, total", 1),
    MK4(428, "mcg", "Menaquinone-4", 1),
    VITK1D(429, "mcg", "Dihydrophylloquinone", 1),
    VITK1(430, "mcg", "Vitamin K (phylloquinone)", 1),
    FOLAC(431, "mcg", "Folic acid", 0),
    FOLFD(432, "mcg", "Folate, food", 0),
    FOLDFE(435, "mcg_DFE", "Folate, DFE", 0),
    BETN(454, "mg", "Betaine", 1),
    TRP_G(501, "g", "Tryptophan", 3),
    THR_G(502, "g", "Threonine", 3),
    ILE_G(503, "g", "Isoleucine", 3),
    LEU_G(504, "g", "Leucine", 3),
    LYS_G(505, "g", "Lysine", 3),
    MET_G(506, "g", "Methionine", 3),
    CYS_G(507, "g", "Cystine", 3),
    PHE_G(508, "g", "Phenylalanine", 3),
    TYR_G(509, "g", "Tyrosine", 3),
    VAL_G(510, "g", "Valine", 3),
    ARG_G(511, "g", "Arginine", 3),
    HISTN_G(512, "g", "Histidine", 3),
    ALA_G(513, "g", "Alanine", 3),
    ASP_G(514, "g", "Aspartic acid", 3),
    GLU_G(515, "g", "Glutamic acid", 3),
    GLY_G(516, "g", "Glycine", 3),
    PRO_G(517, "g", "Proline", 3),
    SER_G(518, "g", "Serine", 3),
    HYP(521, "g", "Hydroxyproline", 3),
    VITE_ADDED(573, "mg", "Vitamin E, added", 2),
    VITB12_ADDED(578, "mcg", "Vitamin B-12, added", 2),
    CHOLE(601, "mg", "Cholesterol", 0),
    FATRN(605, "g", "Fatty acids, total trans", 3),
    FASAT(606, "g", "Fatty acids, total saturated", 3),
    F4D0(607, "g", "4:0", 3),
    F6D0(608, "g", "6:0", 3),
    F8D0(609, "g", "8:0", 3),
    F10D0(610, "g", "10:0", 3),
    F12D0(611, "g", "12:0", 3),
    F14D0(612, "g", "14:0", 3),
    F16D0(613, "g", "16:0", 3),
    F18D0(614, "g", "18:0", 3),
    F20D0(615, "g", "20:0", 3),
    F18D1(617, "g", "18:1 undifferentiated", 3),
    F18D2(618, "g", "18:2 undifferentiated", 3),
    F18D3(619, "g", "18:3 undifferentiated", 3),
    F20D4(620, "g", "20:4 undifferentiated", 3),
    F22D6(621, "g", "22:6 n-3 (DHA)", 3),
    F22D0(624, "g", "22:0", 3),
    F14D1(625, "g", "14:1", 3),
    F16D1(626, "g", "16:1 undifferentiated", 3),
    F18D4(627, "g", "18:4", 3),
    F20D1(628, "g", "20:1", 3),
    F20D5(629, "g", "20:5 n-3 (EPA)", 3),
    F22D1(630, "g", "22:1 undifferentiated", 3),
    F22D5(631, "g", "22:5 n-3 (DPA)", 3),
    PHYSTR(636, "mg", "Phytosterols", 0),
    STID7(638, "mg", "Stigmasterol", 0),
    CAMD5(639, "mg", "Campesterol", 0),
    SITSTR(641, "mg", "Beta-sitosterol", 0),
    FAMS(645, "g", "Fatty acids, total monounsaturated", 3),
    FAPU(646, "g", "Fatty acids, total polyunsaturated", 3),
    F15D0(652, "g", "15:0", 3),
    F17D0(653, "g", "17:0", 3),
    F24D0(654, "g", "24:0", 3),
    F16D1T(662, "g", "16:1 t", 3),
    F18D1T(663, "g", "18:1 t", 3),
    F18D2TT(669, "g", "18:2 t,t", 3),
    F18D2CLA(670, "g", "18:2 CLAs", 3),
    F24D1C(671, "g", "24:1 c", 3),
    F20D2CN6(672, "g", "20:2 n-6 c,c", 3),
    F16D1C(673, "g", "16:1 c", 3),
    F18D1C(674, "g", "18:1 c", 3),
    F18D2CN6(675, "g", "18:2 n-6 c,c", 3),
    F18D3CN6(685, "g", "18:3 n-6 c,c,c", 3),
    F17D1(687, "g", "17:1", 3),
    F20D3(689, "g", "20:3 undifferentiated", 3),
    FATRNM(693, "g", "Fatty acids, total trans-monoenoic", 3),
    FATRNP(695, "g", "Fatty acids, total trans-polyenoic", 3),
    F13D0(696, "g", "13:0", 3),
    F15D1(697, "g", "15:1", 3),
    F18D3CN3(851, "g", "18:3 n-3 c,c,c (ALA)", 3),
    F20D3N3(852, "g", "20:3 n-3", 3),
    F20D3N6(853, "g", "20:3 n-6", 3),
    F20D4N6(855, "g", "20:4 n-6", 3),
    F21D5(857, "g", "21:5", 3),
    F22D4(858, "g", "22:4", 3),
    F18D1TN7(859, "g", "18:1-11t (18:1t n-7)", 3);

    private static final Map<Integer, Nutrient> BY_ID = new HashMap<>();

    static {
        for (Nutrient nutrient : values()) {
            BY_ID.put(nutrient.id, nutrient);
        }
    }

    private final int id;
    private final String unit;
    private final String name;
    private final int decimalPlaces;

    private Nutrient(int id, String unit, String name, int decimalPlaces) {
        this.id = id;
        this.unit = unit;
        this.name = name;
        this.decimalPlaces = decimalPlaces;
    }

    public int getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public static Nutrient forId(int id) {
        return BY_ID.get(id);
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = IOUtils.readLines(Nutrient.class.getResourceAsStream("/data/NUTR_DEF.txt"));

        for (String line : lines) {
            Object[] parts = line.split("\\^");
            for (int i = 0; i < parts.length; i++) {
                String stringPart = (String) parts[i];
                if (stringPart.startsWith("~") && stringPart.endsWith("~")) {
                    // this is a text field
                    parts[i] = stringPart.substring(1, stringPart.length() - 1);
                }
            }
            StringBuilder sb = new StringBuilder(parts[2].toString()).append("(")
                    .append(parts[0])
                    .append(", \"")
                    .append(parts[1])
                    .append("\", \"")
                    .append(parts[3])
                    .append("\", ")
                    .append(parts[4])
                    .append("),");
            System.out.println(sb.toString());
        }
    }
}
