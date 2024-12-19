import java.sql.Connection; // IMPORT UNTUK KONEKSI JDBC
import java.sql.PreparedStatement; // IMPORT UNTUK QUERY JDBC
import java.sql.ResultSet; // IMPORT UNTUK HASIL QUERY JDBC
import java.sql.SQLException; // IMPORT UNTUK EXCEPTION HANDLING JDBC
import java.text.SimpleDateFormat; // IMPORT UNTUK MEMFORMAT DATE
import java.util.Date; // IMPORT UNTUK MANIPULASI DATE

// SUBCLASS: DOKUMENLEGAL, SEBAGAI ANAK DARI KELAS INDUK DOKUMEN
public class DokumenLegal extends Dokumen { 
    // ATRIBUT TAMBAHAN DIKLARASIKAN KHUSUS UNTUK KELAS DOKUMENLEGAL
    private String nomorKontrak; // NOMOR KONTRAK UNTUK IDENTITAS DOKUMEN LEGAL
    private Connection conn; // ATRIBUT UNTUK MENYIMPAN KONEKSI DATABASE

    // KONSTRUKTOR
    public DokumenLegal(String idDokumen, String judul, Date tanggalUpload, String kategori, String pathFile, String nomorKontrak) {
        super(idDokumen, judul, tanggalUpload, kategori, pathFile); // PEMANGGILAN KONSTRUKTOR DARI SUPERCLASS (INHERITANCE)
        this.nomorKontrak = nomorKontrak; // INISIALISASI NOMOR KONTRAK
        this.conn = DatabaseConnection.connect(); // INISIALISASI KONEKSI JDBC
    }

    // GETTER DAN SETTER UNTUK NOMORKONTRAK
    public String getNomorKontrak() {
        return nomorKontrak; // MENGEMBALIKAN NOMOR KONTRAK
    }

    public void setNomorKontrak(String nomorKontrak) {
        this.nomorKontrak = nomorKontrak; // MENGUBAH NILAI NOMOR KONTRAK
    }

    // IMPLEMENTASI METHOD ABSTRAK
    @Override
    public void tampilkanDetail() {
        System.out.println("=== Detail Dokumen Legal ==="); // MENAMPILKAN DETAIL DOKUMEN LEGAL
        System.out.println("ID Dokumen      : " + idDokumen); // MENAMPILKAN ID DOKUMEN
        System.out.println("Judul           : " + judul); // MENAMPILKAN JUDUL DOKUMEN
        System.out.println("Tanggal Upload  : " + tanggalUpload); // MENAMPILKAN TANGGAL UPLOAD
        System.out.println("Kategori        : " + kategori); // MENAMPILKAN KATEGORI DOKUMEN
        System.out.println("Path File       : " + pathFile); // MENAMPILKAN PATH FILE DOKUMEN
        System.out.println("Nomor Kontrak   : " + nomorKontrak); // MENAMPILKAN NOMOR KONTRAK
    }

    // MENAMBAHKAN DATA KE TABEL DOKUMEN_LEGAL
    public void create(DokumenLegal dokumenLegal) {
        String sql = "INSERT INTO dokumen_legal (idDokumen, nomorKontrak) VALUES (?, ?)"; // QUERY UNTUK MENYIMPAN DATA (CRUD - CREATE)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, dokumenLegal.getIdDokumen()); // SET NILAI ID DOKUMEN
            stmt.setString(2, dokumenLegal.getNomorKontrak()); // SET NILAI NOMOR KONTRAK
            stmt.executeUpdate(); // EKSEKUSI QUERY
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    // METODE UNTUK MEMUAT DATA DARI DATABASE
    public static DokumenLegal loadFromDatabase(String id, String judul, Date tanggalUpload, String kategori, String pathFile, Connection conn) {
        String sql = "SELECT nomorKontrak FROM dokumen_legal WHERE idDokumen = ?"; // QUERY UNTUK MEMUAT DATA (CRUD - READ)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, id); // SET NILAI ID DOKUMEN
            ResultSet rs = stmt.executeQuery(); // EKSEKUSI QUERY DAN MENYIMPAN HASIL
            if (rs.next()) { // MEMERIKSA JIKA ADA DATA YANG DITEMUKAN
                String nomorKontrak = rs.getString("nomorKontrak"); // MENGAMBIL NILAI NOMOR KONTRAK DARI HASIL QUERY
                return new DokumenLegal(id, judul, tanggalUpload, kategori, pathFile, nomorKontrak); // MENGEMBALIKAN OBJEK DOKUMENLEGAL
            }
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
        return null; // MENGEMBALIKAN NILAI NULL JIKA DATA TIDAK DITEMUKAN
    }

    // GENERATE ID UNTUK DOKUMEN LEGAL
    public static String generateId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // MEMBUAT FORMAT TIMESTAMP (MANIPULASI DATE)
        String timestamp = sdf.format(new Date()); // MENGAMBIL TIMESTAMP SAAT INI
        return "DL" + timestamp; // MENGEMBALIKAN ID DENGAN FORMAT "DL" DIDEPAN TIMESTAMP
    }
}
