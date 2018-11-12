package ie.ucd.engac;

import java.util.Properties;

public class GameConfig {
    public static int panelWidth;
    public static int panelHeight;
    public static int max_num_players;

    public static String action_card_deck_config_file_location;
    public static String house_card_deck_config_file_location;
    public static String career_card_deck_config_file_location;
    public static String college_career_card_deck_config_file_location;
    public static String game_board_config_file_location;

    public static int loan_amount;
    public static int repayment_amount;
    public static int payday_landed_on_bonus;
    public static int get_married_even_payment;
    public static int get_married_odd_payment;
    public static int night_school_tuition_fees;
    public static int college_upfront_cost;
    public static int spin_to_win_prize_money;
    public static int starting_money;

    public static int ret_bonus_remaining;
    public static int ret_bonus_action;
    public static int ret_bonus_kids;


    public GameConfig(Properties properties){

        panelWidth = Integer.parseInt(properties.getProperty("PANWIDTH"));
        panelHeight = Integer.parseInt(properties.getProperty("PANHEIGHT"));
        max_num_players = Integer.parseInt(properties.getProperty("MAX_NUM_PLAYERS"));

        action_card_deck_config_file_location = properties.getProperty("ACTION_CARD_DECK_CONFIG_FILE_LOCATION");
        house_card_deck_config_file_location = properties.getProperty("HOUSE_CARD_DECK_CONFIG_FILE_LOCATION");
        career_card_deck_config_file_location = properties.getProperty("CAREER_CARD_DECK_CONFIG_FILE_LOCATION");
        college_career_card_deck_config_file_location = properties.getProperty("COLLEGE_CAREER_CARD_DECK_CONFIG_FILE_LOCATION");
        game_board_config_file_location = properties.getProperty("LOGIC_BOARD_CONFIG_FILE_LOCATION");

        loan_amount = Integer.parseInt(properties.getProperty("LOAN_AMOUNT"));
        repayment_amount = Integer.parseInt(properties.getProperty("REPAYMENT_AMOUNT"));
        payday_landed_on_bonus = Integer.parseInt(properties.getProperty("PAYDAY_LANDED_ON_BONUS"));
        get_married_even_payment = Integer.parseInt(properties.getProperty("GET_MARRIED_EVEN_PAYMENT"));
        get_married_odd_payment = Integer.parseInt(properties.getProperty("GET_MARRIED_ODD_PAYMENT"));
        night_school_tuition_fees = Integer.parseInt(properties.getProperty("NIGHT_SCHOOL_TUITION_FEES"));
        college_upfront_cost = Integer.parseInt(properties.getProperty("COLLEGE_UPFRONT_COST"));
        spin_to_win_prize_money = Integer.parseInt(properties.getProperty("SPIN_TO_WIN_PRIZE_MONEY"));
        starting_money = Integer.parseInt(properties.getProperty("STARTING_MONEY"));

        ret_bonus_remaining = Integer.parseInt(properties.getProperty("RET_BONUS_REMAINING"));
        ret_bonus_action = Integer.parseInt(properties.getProperty("RET_BONUS_ACTION"));
        ret_bonus_kids = Integer.parseInt(properties.getProperty("RET_BONUS_KIDS"));

    }
}
