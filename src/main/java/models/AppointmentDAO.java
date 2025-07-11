/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import credentials.*;

import java.sql.*;
import java.util.*;

/**
 *
 * @author lenovo
 */
public class AppointmentDAO {
    private Connection connectionToDB;
    public AppointmentDAO() {
        connectionToDB = DBCredentials.getCredentials().getConnection();
    }
    public List<Appointment> getAllAppointments() {
        try{
            List<Appointment> appointments = new ArrayList<>();
            String query = "SELECT * FROM Appointment";
            Statement statement = connectionToDB.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("Id"),
                    rs.getTimestamp("DateTime"),
                    rs.getString("ReasonToVisit"),
                    rs.getString("Status"),
                    rs.getInt("PatientID"),
                    rs.getInt("DoctorID")
                ));
            }
            return appointments;
        } catch (SQLException e) {
            System.err.println("AppointmentDAO : Error while calling 'getAllAppointments' ");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Appointment> getAllAppointmentsOfPatient(int id) {
        try{
            List<Appointment> appointments = new ArrayList<>();
            String query = "SELECT * FROM Appointment WHERE PatientID = " + id;
            Statement statement = connectionToDB.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("Id"),
                    rs.getTimestamp("DateTime"),
                    rs.getString("ReasonToVisit"),
                    rs.getString("Status"),
                    rs.getInt("PatientID"),
                    rs.getInt("DoctorID")
                ));
            }
            return appointments;
        } catch (SQLException e) {
            System.err.println("AppointmentDAO : Error while calling 'getAllAppointmentsOfPatient' ");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void insertAppointment(Appointment a) {
        try {
            String query = "INSERT INTO Appointment(DateTime, ReasonToVisit, Status, PatientID, DoctorID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connectionToDB.prepareStatement(query);
            ps.setTimestamp(1, new java.sql.Timestamp(a.getDateTime().getTime()));
            ps.setString(2, a.getReasonToVisit());
            ps.setString(3, a.getStatus());
            ps.setInt(4, a.getPatientId());
            ps.setInt(5, a.getDoctorId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("AppointmentDAO : Error while calling 'getAllAppointments' ");
            e.printStackTrace();
        }
    }
    // This function is used to get the number of 'rendez-vous' for the current day
    public int getNumberOfAppointmentsForToday() {
        try {
            String query = "SELECT COUNT(*) FROM Appointment WHERE DATE(DateTime) = (SELECT CURDATE())";
            Statement statement = connectionToDB.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }else {
                throw new SQLException("AppointmentDAO : Error while calling 'getNumberOfAppointmentsForToday' ");
            }
            
        } catch (SQLException e) {
            System.err.println("AppointmentDAO : Error while calling 'getNumberOfAppointmentsForToday' ");
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @author amine
     * @param year 
     * @param month
     * @param day
     * @return a list of appointments of a given date
     * @throws SQLException
     */
    public List<Appointment> getAppointmentsByDate(String year, String month, String day) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();

        // Format 
        String dateStr = String.format("%s-%02d-%02d", year, Integer.parseInt(month), Integer.parseInt(day));

        String query = "SELECT * FROM Appointment WHERE DATE(DateTime) = ?";
        PreparedStatement ps = connectionToDB.prepareStatement(query);
        ps.setString(1, dateStr);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            appointments.add(new Appointment(
                rs.getInt("Id"),
                rs.getTimestamp("DateTime"),
                rs.getString("ReasonToVisit"),
                rs.getString("Status"),
                rs.getInt("PatientID"),
                rs.getInt("DoctorID")
            ));
        }

        return appointments;
    }
    public void updateAppointment(Appointment appointment) throws SQLException {
        String query = "UPDATE Appointment SET DateTime = ?, ReasonToVisit = ?, Status = ?, PatientID = ?, DoctorID = ? WHERE Id = ?";

        try (PreparedStatement ps = connectionToDB.prepareStatement(query)) {
            ps.setTimestamp(1, new java.sql.Timestamp(appointment.getDateTime().getTime()));
            ps.setString(2, appointment.getReasonToVisit());
            ps.setString(3, appointment.getStatus());
            ps.setInt(4, appointment.getPatientId());
            ps.setInt(5, appointment.getDoctorId());
            ps.setInt(6, appointment.getId());

            ps.executeUpdate();
        }
    }
    /**
     * delete an appointment based on the id 
     * @param id of the appointment
     * @throws SQLException
     */
    public void deleteAppointment(int id) throws SQLException {
        String query = "DELETE FROM Appointment WHERE ID = ?";
        PreparedStatement ps = connectionToDB.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }


}
