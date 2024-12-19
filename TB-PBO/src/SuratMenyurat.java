import java.sql.Connection; // IMPORT UNTUK KONEKSI JDBC
import java.sql.PreparedStatement; // IMPORT UNTUK QUERY JDBC
import java.sql.ResultSet; // IMPORT UNTUK HASIL QUERY JDBC
import java.sql.SQLException; // IMPORT UNTUK EXCEPTION HANDLING JDBC
import java.text.SimpleDateFormat; // IMPORT UNTUK MEMFORMAT DATE
import java.util.Date; // IMPORT UNTUK MANIPULASI DATE

public class SuratMenyurat extends Dokumen { // SUBCLASS: SURATMENYURAT, SEBAGAI ANAK DARI KELAS INDUK DOKUMEN
    private String nomorSurat; // NOMOR SURAT PADA DOKUMEN SURAT MENYURAT
    private String penerima; // PENERIMA DARI SURAT MENYURAT
    private Connection conn; // KONEKSI DATABASE UNTUK INTERAKSI DENGAN DB

    // KONSTRUKTOR
    public SuratMenyurat(String idDokumen, String judul, Date tanggalUpload, String kategori, String pathFile, String nomorSurat, String penerima) {
        super(idDokumen, judul, tanggalUpload, kategori, pathFile); // PEMANGGILAN KONSTRUKTOR DARI SUPERCLASS (INHERITANCE)
        this.nomorSurat = nomorSurat; // INISIALISASI NOMOR SURAT
        this.penerima = penerima; // INISIALISASI PENERIMA
        this.conn = DatabaseConnection.connect(); // INISIALISASI KONEKSI JDBC
    }

    // GETTER DAN SETTER UNTUK NOMOR SURAT DAN PENERIMA
    public String getNomorSurat() {
        return nomorSurat; // MENGEMBALIKAN NOMOR SURAT
    }

    public void setNomorSurat(String nomorSurat) {
        this.nomorSurat = nomorSurat; // MENGUBAH NILAI NOMOR SURAT
    }

    public String getPenerima() {
        return penerima; // MENGEMBALIKAN PENERIMA
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima; // MENGUBAH NILAI PENERIMA
    }

    // IMPLEMENTASI METHOD ABSTRAK
    @Override
    public void tampilkanDetail() {
        System.out.println("=== Detail Surat Menyurat ==="); // MENAMPILKAN DETAIL SURAT MENYURAT
        System.out.println("ID Dokumen      : " + idDokumen); // MENAMPILKAN ID DOKUMEN
        System.out.println("Judul           : " + judul); // MENAMPILKAN JUDUL DOKUMEN
        System.out.println("Tanggal Upload  : " + tanggalUpload); // MENAMPILKAN TANGGAL UPLOAD
        System.out.println("Kategori        : " + kategori); // MENAMPILKAN KATEGORI DOKUMEN
        System.out.println("Path File       : " + pathFile); // MENAMPILKAN PATH FILE DOKUMEN
        System.out.println("Nomor Surat     : " + nomorSurat); // MENAMPILKAN NOMOR SURAT
        System.out.println("Penerima        : " + penerima); // MENAMPILKAN PENERIMA
    }

    // METODE UNTUK MENYIMPAN DATA KE TABEL SURAT_MENYURAT
    public void create(SuratMenyurat suratMenyurat) {
        String sql = "INSERT INTO surat_menyurat (idDokumen, nomorSurat, penerima) VALUES (?, ?, ?)"; // QUERY UNTUK MENYIMPAN DATA (CRUD - CREATE)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, suratMenyurat.getIdDokumen()); // SET NILAI ID DOKUMEN
            stmt.setString(2, suratMenyurat.getNomorSurat()); // SET NILAI NOMOR SURAT
            stmt.setString(3, suratMenyurat.getPenerima()); // SET NILAI PENERIMA
            stmt.executeUpdate(); // EKSEKUSI QUERY
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    // METODE UNTUK MEMUAT DATA DARI DATABASE
    public static SuratMenyurat loadFromDatabase(String id, String judul, Date tanggalUpload, String kategori, String pathFile, Connection conn) {
        String sql = "SELECT nomorSurat, penerima FROM surat_menyurat WHERE idDokumen = ?"; // QUERY UNTUK MEMUAT DATA (CRUD - READ)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, id); // SET NILAI ID DOKUMEN
            ResultSet rs = stmt.executeQuery(); // EKSEKUSI QUERY DAN MENYIMPAN HASIL
            if (rs.next()) { // MEMERIKSA JIKA ADA DATA YANG DITEMUKAN
                String nomorSurat = rs.getString("nomorSurat"); // MENGAMBIL NILAI NOMOR SURAT DARI HASIL QUERY
                String penerima = rs.getString("penerima"); // MENGAMBIL NILAI PENERIMA DARI HASIL QUERY
                return new SuratMenyurat(id, judul, tanggalUpload, kategori, pathFile, nomorSurat, penerima); // MENGEMBALIKAN OBJEK SURATMENYURAT
            }
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
        return null; // MENGEMBALIKAN NILAI NULL JIKA DATA TIDAK DITEMUKAN
    }

    // GENERATE ID UNTUK SURAT MENYURAT
    public static String generateId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // MEMBUAT FORMAT TIMESTAMP (MANIPULASI DATE)
        String timestamp = sdf.format(new Date()); // MENGAMBIL TIMESTAMP SAAT INI
        return "SM" + timestamp; // MENGEMBALIKAN ID DENGAN FORMAT "SM" DIDEPAN TIMESTAMP
    }
}
