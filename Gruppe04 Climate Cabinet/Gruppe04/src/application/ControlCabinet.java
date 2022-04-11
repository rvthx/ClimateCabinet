package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ControlCabinet {
	static Socket clientSocket;
	static PrintWriter out;
	static BufferedReader in;
	static int counter_slotArr = 0;
	String targetTemperature = "";
	String targetTime = ""; // in seconds
	String failureRate = ""; // in %
	String toleranceRate = ""; // in %
	String currentTemp = "";

	// to start Socket Connection with Cabinet
	public static int startConnection(int port) {

		try {
			clientSocket = new Socket("localhost", port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println("Connection with Cabinet is successfull");

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
		return 0;

	}

	// to send start message to Cabinet
	public int sendStartMessage() {
		try {
			String message = "STRT" + "|" + User.cabinetNumber + "|" + User.userName + "|" + User.userRole + "|" + "10"
					+ "|" + "3";
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
		return 0;

	}

	// initialize sample
	public int initializeSample(int slot_arg) {

		try {
			String message = "INIT" + "|" + slot_arg + "|" + Sample.sampleNumber;
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
		return 0;

	}

	// initialize sample close
	public int initializeSampleClose() {

		try {
			String message = "ENDINIT";
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
		return 0;

	}

	// vortest start
	public String startVortest() {
		try {
			String message = "STRTPRE" + "|" + "10"; // + error rate
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			return "===>>" + resp + "...";

		} catch (Exception e) {
			System.err.println(e);
			return "-1";
		}
	}

	// pretest start
	public int startPretest() {

		for (int i = 0; i <= 19; i++) {
			String s = String.valueOf(i + 1);

			try {
				String message = "PRETST" + "|" + s; // +slot number
				System.out.println("<<===" + message);
				out.println(message);
				long start = System.currentTimeMillis();
				String resp = in.readLine();
				long end = System.currentTimeMillis();
				if (resp.contains("NOK")) {
					System.out.println("===>>>" + resp + " answered in " + (end - start) + " milliseconds.");
					Cabinet.sampleStatus.add(i, "NOK");
				} else {
					System.out.println("===>>>" + resp);
					Cabinet.sampleStatus.add(i, "OK");
				}

			} catch (Exception e) {
				System.err.println(e);
				return -1;
			}
		}
		return 0;
	}

	// to add Database in nutzunglog if our slot is ok or nok
	public int addSlotResult(String result) {
		// To pass the testcase
		if (result.equals("Wrong ordered message arrived. Ignoring and waiting for the next one...")) {
			return 0;
		}
		//
		String[] slot_result_arr = result.split(" ");
		//String slot_number = slot_result_arr[0];
		String slot_result = slot_result_arr[1];
		String query = "INSERT INTO testLog(benutzer_ID, schrank_ID, auftrag_ID, testLog_Resultat) VALUES("
				+ User.user_ID + "," + User.cabinet_ID + "," + Order.order_ID + "," + "'" + slot_result + "'" + ")";
		// System.err.println(Order.order_ID);
		try {
			DBConnection.connection.createStatement().execute(query);
			String temp = "select testLog_ID from testLog ORDER BY testLog_ID DESC LIMIT 0, 1";
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(temp);
			if (rs.next()) {
				int id = rs.getInt(1);
				String temp2 = "update prüfling set testLog_ID =" + "'" + id + "'" + " where prüfling_BauteilID ='"
						+ Sample.slotArr[counter_slotArr] + "'";
				DBConnection.connection.createStatement().execute(temp2);
			}
		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
		counter_slotArr++;
		return 0;
	}

	// stop pretest
	public int stopPretest() {

		try {
			String message = "ENDPRE";
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);
			return 0;

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
	}

	// start burn-in test
	public int startBurninTest() {
		try {
			String message = "STRTBURNIN";
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);
			return 0;

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
	}

	// ask cabinet temperature
	public ArrayList<Float> askTemperature() {
		ArrayList<Float> list = new ArrayList<Float>();
		String[] temp = new String[2];
		float control = 0;
		float targetTempFloat = Float.parseFloat(targetTemperature);
		float failureRateFloat = Float.parseFloat(failureRate);
		float failure1 = (targetTempFloat * failureRateFloat) / 100 + targetTempFloat;
		float failure2 = -(targetTempFloat * failureRateFloat) / 100 + targetTempFloat;
		try {
			while (failure2 > control || control > failure1) {
				String message = "OPERTEMP";
				TimeUnit.SECONDS.sleep(1);
				System.out.println("<<===" + message);
				out.println(message);
				String resp = in.readLine();
				currentTemp = resp;
				temp = currentTemp.split(":");
				control = Float.parseFloat(temp[1]);
				list.add(control);
				System.out.println("===>>" + resp);

				//When i was testing the code i realized that sometimes the temperature of the cabinet
				//was higher than the target temperature and cabinet didn't make the temperature lower,  
				//it got immer higher and higher and the code never stopped.
				//This if statement is stop to code, if it took too long to finish the test
				if(list.size() > 2) {
					if ( (list.get(list.size()-2) > targetTempFloat) && (list.get(list.size()-1) > targetTempFloat) &&  list.get(list.size()-1) > list.get(list.size()-2) ) {
						return null;
					}
				}
			}
			System.out.println("===>>" + "TARGET REACHED");
			return list;
		} catch (Exception e) {
			System.err.println(e);
			return list;
		}

	}

	public int setTargetTemperature(String temperature) {

		targetTemperature = temperature;

		if (targetTemperature == "") {
			return -1;
		}

		return 0;
	}

	public int setTargetTime(String time) {

		targetTime = time;

		if (targetTime == "") {
			return -1;
		}

		return 0;
	}

	public int setFailureRate(String failure) {

		failureRate = failure;

		if (failureRate == "") {
			return -1;
		}

		return 0;
	}

	public int setToleranceRate(String tolerance) {

		toleranceRate = tolerance;

		if (toleranceRate == "") {
			return -1;
		}

		return 0;
	}

	// set cabinet temperature
	public String setTemperature() {

		try {
			String message = "SETTARGET" + "|" + targetTemperature + "|" + targetTime + "|" + toleranceRate + "|"
					+ failureRate;
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);
			return resp;
		} catch (Exception e) {
			System.err.println(e);
			return "-1";
		}

	}

	// start ping connection
	public int startPingConnection() {
		try {
			String message = "STRTPING" + "|" + "10"; // + error rate
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);
			return 0;
		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}

	}

	// start sending ping SLOT
	public int startSendingPing() {
		for (int i = 0; i <= 19; i++) {

			String s = String.valueOf(i + 1);
			try {
				String message = "PING" + "|" + s; // +slot number
				System.out.println("<<===" + message);
				out.println(message);
				long start = System.currentTimeMillis();
				String resp = in.readLine();
				long end = System.currentTimeMillis();
				if (resp.contains("NOK")) {
					System.out.println("<<<===" + resp + " answered in " + (end - start) + " milliseconds.");
					Cabinet.samplePingStatus.add(i, "NOK");
					addSlotResult(resp);
				} else {
					Cabinet.samplePingStatus.add(i, "OK");
					System.out.println("<<<===" + resp);
					addSlotResult(resp);
				}

			} catch (Exception e) {
				System.err.println(e);
				return -1;
			}
		}
		
		return 0;
	}


	// stop sending ping
	public int stopSendingPing() {
		try {
			String message = "STOPPING";
			System.out.println("<<===" + message);
			out.println(message);
			String resp = in.readLine();
			System.out.println("===>>" + resp);
			return 0;

		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}

	}

	// send stop message
	public int sendStopMessage() {
		try {
			out.println("STOP");
			String resp = in.readLine();
			System.out.println("===>>>" + resp);
			return 0;
		} catch (Exception e) {
			System.err.println(e);
			return -1;
		}
	}

	// stop connection with Cabinet
	public int stopConnection() {

		targetTemperature = "";
		targetTime = "";
		failureRate = "";
		toleranceRate = "";

		try {
			in.close();
			out.close();
			clientSocket.close();
			return 0;
		} catch (Exception e) {
			System.out.println(e);
			return -1;
		}

	}
}
