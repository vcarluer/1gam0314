package gamers.associate;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.lang.NumberFormatException;

public class MythicEvent {
	public static void main(String[] args) {
		System.out.println("Mythic pick");
		Random rand = new Random();

		Scanner scanIn = new Scanner(System.in);
		int choice = 0;
		while (choice != 4) {
			System.out.println("Choose roll:");
			System.out.println("1: Random event");
			System.out.println("2: Modify the scene");
			System.out.println("3: Yes/No question");
			System.out.println("4: Quit");

			try {
				choice = Integer.parseInt(scanIn.nextLine());
			} catch (NumberFormatException e) {
				choice = 1;
			}

			if (choice < 1 || choice > 4) choice = 1;
			int chaos = 5;
			if (choice == 2 || choice == 3) {

				System.out.println("Chaos factor (1-9): ");
				try {
					chaos = Integer.parseInt(scanIn.nextLine());
				} catch (NumberFormatException e) {
					chaos = 5;
				}
			}

			if (choice == 1) {
				printRandomEvent(rand);
			}

			if (choice == 2) {
				System.out.println();
				int sceneChange = rand.nextInt(9) + 1;
				System.out.println("ROLL " + String.valueOf(sceneChange));
				if (sceneChange <= chaos) {
					if (sceneChange % 2 == 0) {
						System.out.println("INTERRUPT scene");
					} else {
						System.out.println("ALTERED scene");
					}

					printRandomEvent(rand);
				} else {
					System.out.println("no change");
				System.out.println();
				}
			}

			if (choice == 3) {
				System.out.println();
				System.out.println("Ask a yes/no question: ");
				String question;
				question = scanIn.nextLine();

				System.out.println();
				System.out.println("Choose the odds:");
				ArrayList<String> odds = getFateOdds();
				int i = 0;
				for (String odd : odds) {
					System.out.println(String.valueOf(i) + ": " + odd);
					i++;
				}

				int odd;
				try
				{
					odd = Integer.parseInt(scanIn.nextLine());
				} catch (NumberFormatException e) {
					odd = 4;
				}

				System.out.println();
				int fateChartRoll = rand.nextInt(100);
				String strFate = String.valueOf(fateChartRoll);
				System.out.println("ROLL " + strFate);
				fateRoll(chaos, odd, fateChartRoll);
				if (strFate.length() > 1 && strFate.charAt(0) == strFate.charAt(1)) {
					System.out.println(strFate + ": FATE chart random event!!");
					printRandomEvent(rand);
				} else {
					System.out.println();
				}
			}
		}

		scanIn.close();
	}

	private static void printRandomEvent(Random rand) {
		ArrayList<String> focus = getEventFocus();
		ArrayList<String> actions = getEventMeaningActions();		
		ArrayList<String> subjects = getEventMeaningSubjects();
		System.out.println();
		int percentRand = rand.nextInt(100);
		int focusIdx = getEventFocusIndex(percentRand);
		String focusName = focus.get(focusIdx);
		System.out.println("Focus: " + focusName);

		int actionIdx = rand.nextInt(actions.size());
		String actionName = actions.get(actionIdx);
		System.out.println("Action: " + actionName);

		int subjectIdx = rand.nextInt(subjects.size());
		String subjectName = subjects.get(subjectIdx);
		System.out.println("Subject: " + subjectName);
		System.out.println();
	}


	private static int[][] fateChart = {
				{ -20, 0, 5, 5, 10, 20, 25, 45, 50, 55, 80 },
				{ 0, 5, 5, 10, 15, 25, 35, 50, 55, 65, 85 },
				{ 0, 5, 10, 15, 25, 45, 50, 65, 75, 80, 90 },
				{ 5, 10, 15, 20, 35, 50, 55, 75, 80, 85, 95 },
				{ 5, 15, 25, 35, 50, 65, 75, 85, 90, 90, 95 },
				{ 10, 25, 45, 50, 65, 80, 85, 90, 95, 95, 100 },
				{ 15, 35, 50, 55, 75, 85, 90, 95, 95, 95, 100 },
				{ 25, 50, 65, 75, 85, 90, 95, 95, 100, 110, 130 },
				{ 50, 75, 85, 90, 95, 95, 100, 105, 115, 125, 145 }
			};

	private static void fateRoll(int chaos, int odd, int roll) {
		int chaosIdx = chaos - 1;
		if (fateChart != null && chaos <= fateChart.length && odd <= fateChart[chaosIdx].length) {
			int max = fateChart[chaosIdx][odd];
			System.out.println("% success: " + String.valueOf(max));
			if (roll <= max) {
				System.out.println("YES");
				if (roll <= (max / 5)) {
					System.out.println("Exceptionnal!");
				}
			} else {
				System.out.println("NO");
				if (roll >= (100 - ((100 - max) / 5))) {
					System.out.println("Exceptionnal!");
				}
			}
		}
	}

	private static ArrayList<String> getFateOdds() {
		ArrayList<String> level = new ArrayList<String>();
		level.add("Impossible");
		level.add("No way");
		level.add("Very unlikely");
		level.add("Unlikely");
		level.add("50/50");	
		level.add("Somewhat likely");	
		level.add("Likely");	
		level.add("Very likely");	
		level.add("Near sure thing");	
		level.add("A sure thing");	
		level.add("Has to be");

		return level;
	}

	private static ArrayList<String> getEventFocus() {
		ArrayList<String> focus = new ArrayList<String>();
		focus.add("Remote event");
		focus.add("NPC action");
		focus.add("Introduce a new NPC");
		focus.add("Move toward a thread");
		focus.add("Move away from a thread");
		focus.add("Close a thread");
		focus.add("PC negative");
		focus.add("PC positive");
		focus.add("Ambiguous event");
		focus.add("NPC negative");
		focus.add("NPC positive");
		return focus;
	}

	private static int getEventFocusIndex(int percentrand) {
		if (percentrand >= 0 && percentrand <= 6) return 0;
		if (percentrand >= 7 && percentrand <= 27) return 1;
		if (percentrand >= 28 && percentrand <= 34) return 2;
		if (percentrand >= 35 && percentrand <= 44) return 3;
		if (percentrand >= 45 && percentrand <= 51) return 4;
		if (percentrand >= 52 && percentrand <= 54) return 5;
		if (percentrand >= 55 && percentrand <= 66) return 6;
		if (percentrand >= 67 && percentrand <= 74) return 7;
		if (percentrand >= 75 && percentrand <= 82) return 8;
		if (percentrand >= 83 && percentrand <= 91) return 9;
		if (percentrand >= 92 && percentrand <= 99) return 10;

		return 0;
	}

	private static ArrayList<String> getEventMeaningActions() {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("Attainment");
		actions.add("Starting");
		actions.add("Neglect ");
		actions.add("Fight");
		actions.add("Recruit");
		actions.add("Triumph");
		actions.add("Violate");
		actions.add("Oppose");
		actions.add("Malice");
		actions.add("Communicate");
		actions.add("Persecute");
		actions.add("Increase");
		actions.add("Decrease");
		actions.add("Abandon");
		actions.add("Gratify");
		actions.add("Inquire");
		actions.add("Antagonise");
		actions.add("Move");
		actions.add("Waste");
		actions.add("Truce");
		actions.add("Release");
		actions.add("Befriend");
		actions.add("Judge");
		actions.add("Desert");
		actions.add("Dominate");
		actions.add("Procrastinate");
		actions.add("Praise");
		actions.add("Separate");
		actions.add("Take");
		actions.add("Break");
		actions.add("Heal");
		actions.add("Delay");
		actions.add("Stop");
		actions.add("Lie");
		actions.add("Return");
		actions.add("Immitate");
		actions.add("Struggle");
		actions.add("Inform");
		actions.add("Bestow");
		actions.add("Postpone");
		actions.add("Expose");
		actions.add("Haggle");
		actions.add("Imprison");
		actions.add("Release");
		actions.add("Celebrate");
		actions.add("Develop");
		actions.add("Travel");
		actions.add("Block");
		actions.add("Harm");
		actions.add("Debase");
		actions.add("Overindulge");
		actions.add("Adjourn");
		actions.add("Adversity");
		actions.add("Kill");
		actions.add("Disrupt");
		actions.add("Usurp");
		actions.add("Create");
		actions.add("Betray");
		actions.add("Agree");
		actions.add("Abuse");
		actions.add("Oppress");
		actions.add("Inspect");
		actions.add("Ambush");
		actions.add("Spy");
		actions.add("Attach");
		actions.add("Carry");
		actions.add("Open");
		actions.add("Carelessness");
		actions.add("Ruin");
		actions.add("Extravagance");
		actions.add("Trick");
		actions.add("Arrive");
		actions.add("Propose");
		actions.add("Divide");
		actions.add("Refuse");
		actions.add("Mistrust");
		actions.add("Deceive");
		actions.add("Cruelty");
		actions.add("Intolerance");
		actions.add("Trust");
		actions.add("Excitement");
		actions.add("Activity");
		actions.add("Assist");
		actions.add("Care");
		actions.add("Negligence");
		actions.add("Passion");
		actions.add("Work hard");
		actions.add("Control");
		actions.add("Attract");
		actions.add("Failure");
		actions.add("Pursue");
		actions.add("Vengeance");
		actions.add("Proceedings");
		actions.add("Dispute");
		actions.add("Punish");
		actions.add("Guide");
		actions.add("Transform");
		actions.add("Overthrow");
		actions.add("Oppress");
		actions.add("Change");

		return actions;
	}

	private static ArrayList<String> getEventMeaningSubjects() {
		ArrayList<String> subjects = new ArrayList<String>();
		subjects.add("Goals");
		subjects.add("Dreams");
		subjects.add("Environment");
		subjects.add("Outside");
		subjects.add("Inside");
		subjects.add("Reality");
		subjects.add("Allies");
		subjects.add("Enemies");
		subjects.add("Evil ");
		subjects.add("Good");
		subjects.add("Emotions");
		subjects.add("Opposition");
		subjects.add("War");
		subjects.add("Peace");
		subjects.add("The innocent");
		subjects.add("Love");
		subjects.add("The spiritual");
		subjects.add("The intellectual");
		subjects.add("New ideas");
		subjects.add("Joy");
		subjects.add("Messages");
		subjects.add("Energy");
		subjects.add("Balance");
		subjects.add("Tension");
		subjects.add("Friendship");
		subjects.add("The physical");
		subjects.add("A project");
		subjects.add("Pleasures");
		subjects.add("Pain");
		subjects.add("Possessions");
		subjects.add("Benefits");
		subjects.add("Plans");
		subjects.add("Lies");
		subjects.add("Expectations");
		subjects.add("Legal matters");
		subjects.add("Bureaucracy");
		subjects.add("Business ");
		subjects.add("A path");
		subjects.add("News");
		subjects.add("Exterior factors");
		subjects.add("Advice");
		subjects.add("A plot");
		subjects.add("Competition");
		subjects.add("Prison");
		subjects.add("Illness");
		subjects.add("Food");
		subjects.add("Attention");
		subjects.add("Success");
		subjects.add("Failure");
		subjects.add("Travel");
		subjects.add("Jealousy");
		subjects.add("Dispute");
		subjects.add("Home");
		subjects.add("Investment");
		subjects.add("Suffering");
		subjects.add("Wishes");
		subjects.add("Tactics");
		subjects.add("Stalemate");
		subjects.add("Randomness");
		subjects.add("Misfortune");
		subjects.add("Death");
		subjects.add("Disruption");
		subjects.add("Power");
		subjects.add("A burden");
		subjects.add("Intrigues");
		subjects.add("Fears");
		subjects.add("Ambush");
		subjects.add("Rumor");
		subjects.add("Wounds ");
		subjects.add("Extravagance");
		subjects.add("A representative");
		subjects.add("Adversities");
		subjects.add("Opulence");
		subjects.add("Liberty");
		subjects.add("Military");
		subjects.add("The mundane");
		subjects.add("Trials");
		subjects.add("Masses");
		subjects.add("Vehicle");
		subjects.add("Art");
		subjects.add("Victory");
		subjects.add("Dispute");
		subjects.add("Riches");
		subjects.add("Status quo");
		subjects.add("Technology");
		subjects.add("Hope");
		subjects.add("Magic");
		subjects.add("Illusions");
		subjects.add("Portals");
		subjects.add("Danger");
		subjects.add("Weapons");
		subjects.add("Animals");
		subjects.add("Weather");
		subjects.add("Elements");
		subjects.add("Nature");
		subjects.add("The public");
		subjects.add("Leadership");
		subjects.add("Fame");
		subjects.add("Anger");
		subjects.add("Information");

		return subjects;
	}	
}
