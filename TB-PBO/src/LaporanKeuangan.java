import java.sql.Connection; // IMPORT UNTUK KONEKSI JDBC
import java.sql.PreparedStatement; // IMPORT UNTUK QUERY JDBC
import java.sql.ResultSet; // IMPORT UNTUK HASIL QUERY JDBC
import java.sql.SQLException; // IMPORT UNTUK EXCEPTION HANDLING JDBC
import java.text.SimpleDateFormat; // IMPORT UNTUK MEMFORMAT DATE
import java.util.Date; // IMPORT UNTUK MANIPULASI DATE

public class LaporanKeuangan extends Dokumen { // SUBCLASS: LAPORANKEUANGAN, SEBAGAI ANAK DARI KELAS INDUK DOKUMEN
    private double jumlahTransaksi; // JUMLAH TRANSAKSI PADA LAPORAN KEUANGAN
    private String jenisTransaksi; // JENIS TRANSAKSI (PENDAPATAN ATAU PENGELUARAN)
    private Connection conn; // KONEKSI DATABASE UNTUK INTERAKSI DENGAN DB

    // KONSTRUKTOR
    public LaporanKeuangan(String idDokumen, String judul, Date tanggalUpload, String kategori, String pathFile, double jumlahTransaksi, String jenisTransaksi) {
        super(idDokumen, judul, tanggalUpload, kategori, pathFile); // PEMANGGILAN KONSTRUKTOR DARI SUPERCLASS (INHERITANCE)
        this.jumlahTransaksi = jumlahTransaksi; // INISIALISASI JUMLAH TRANSAKSI
        this.jenisTransaksi = jenisTransaksi; // INISIALISASI JENIS TRANSAKSI
        this.conn = DatabaseConnection.connect(); // INISIALISASI KONEKSI JDBC
    }

    // GETTER DAN SETTER UNTUK JUMLAH TRANSAKSI DAN JENIS TRANSAKSI
    public double getJumlahTransaksi() {
        return jumlahTransaksi; // MENGEMBALIKAN JUMLAH TRANSAKSI
    }

    public void setJumlahTransaksi(double jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi; // MENGUBAH NILAI JUMLAH TRANSAKSI
    }

    public String getJenisTransaksi() {
        return jenisTransaksi; // MENGEMBALIKAN JENIS TRANSAKSI
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi; // MENGUBAH NILAI JENIS TRANSAKSI
    }

    // IMPLEMENTASI METHOD ABSTRAK
    @Override
    public void tampilkanDetail() {
        System.out.println("=== Detail Laporan Keuangan ==="); // MENAMPILKAN DETAIL LAPORAN KEUANGAN
        System.out.println("ID Dokumen      : " + idDokumen); // MENAMPILKAN ID DOKUMEN
        System.out.println("Judul           : " + judul); // MENAMPILKAN JUDUL DOKUMEN
        System.out.println("Tanggal Upload  : " + tanggalUpload); // MENAMPILKAN TANGGAL UPLOAD
        System.out.println("Kategori        : " + kategori); // MENAMPILKAN KATEGORI DOKUMEN
        System.out.println("Path File       : " + pathFile); // MENAMPILKAN PATH FILE DOKUMEN
        System.out.println("Jumlah Transaksi: " + jumlahTransaksi); // MENAMPILKAN JUMLAH TRANSAKSI
        System.out.println("Jenis Transaksi : " + jenisTransaksi); // MENAMPILKAN JENIS TRANSAKSI
    }

    // METODE UNTUK MENYIMPAN DATA KE TABEL LAPORAN_KEUANGAN
    public void create(LaporanKeuangan laporanKeuangan) {
        String sql = "INSERT INTO laporan_keuangan (idDokumen, jumlahTransaksi, jenisTransaksi) VALUES (?, ?, ?)"; // QUERY UNTUK MENYIMPAN DATA (CRUD - CREATE)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, laporanKeuangan.getIdDokumen()); // SET NILAI ID DOKUMEN
            stmt.setDouble(2, laporanKeuangan.getJumlahTransaksi()); // SET NILAI JUMLAH TRANSAKSI
            stmt.setString(3, laporanKeuangan.getJenisTransaksi()); // SET NILAI JENIS TRANSAKSI
            stmt.executeUpdate(); // EKSEKUSI QUERY
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    // METODE UNTUK MEMUAT DATA DARI DATABASE
    public static LaporanKeuangan loadFromDatabase(String id, String judul, Date tanggalUpload, String kategori, String pathFile, Connection conn) {
        String sql = "SELECT jumlahTransaksi, jenisTransaksi FROM laporan_keuangan WHERE idDokumen = ?"; // QUERY UNTUK MEMUAT DATA (CRUD - READ)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, id); // SET NILAI ID DOKUMEN
            ResultSet rs = stmt.executeQuery(); // EKSEKUSI QUERY DAN MENYIMPAN HASIL
            if (rs.next()) { // MEMERIKSA JIKA ADA DATA YANG DITEMUKAN
                double jumlahTransaksi = rs.getDouble("jumlahTransaksi"); // MENGAMBIL NILAI JUMLAH TRANSAKSI DARI HASIL QUERY
                String jenisTransaksi = rs.getString("jenisTransaksi"); // MENGAMBIL NILAI JENIS TRANSAKSI DARI HASIL QUERY
                return new LaporanKeuangan(id, judul, tanggalUpload, kategori, pathFile, jumlahTransaksi, jenisTransaksi); // MENGEMBALIKAN OBJEK LAPORANKEUANGAN
            }
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
        return null; // MENGEMBALIKAN NILAI NULL JIKA DATA TIDAK DITEMUKAN
    }

    // GENERATE ID UNTUK LAPORAN KEUANGAN
    public static String generateId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // MEMBUAT FORMAT TIMESTAMP (MANIPULASI DATE)
        String timestamp = sdf.format(new Date()); // MENGAMBIL TIMESTAMP SAAT INI
        return "LK" + timestamp; // MENGEMBALIKAN ID DENGAN FORMAT "LK" DIDEPAN TIMESTAMP
    }
}
