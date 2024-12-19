import java.sql.Connection; // IMPORT UNTUK KONEKSI JDBC
import java.sql.PreparedStatement; // IMPORT UNTUK QUERY JDBC
import java.sql.ResultSet; // IMPORT UNTUK HASIL QUERY JDBC
import java.sql.SQLException; // IMPORT UNTUK EXCEPTION HANDLING JDBC
import java.text.SimpleDateFormat; // IMPORT UNTUK MEMFORMAT DATE
import java.util.Date; // IMPORT UNTUK MANIPULASI DATE

// SUBCLASS: DOKUMENPRIBADI, SEBAGAI ANAK DARI KELAS INDUK DOKUMEN
public class DokumenPribadi extends Dokumen {
    private String namaPemilik; // NAMA PEMILIK DOKUMEN PRIBADI
    private String deskripsi; // DESKRIPSI DOKUMEN PRIBADI
    private Connection conn; // KONEKSI DATABASE UNTUK INTERAKSI DENGAN DB

    // KONSTRUKTOR // 5. MANIPULASI DATE
    public DokumenPribadi(String idDokumen, String judul, Date tanggalUpload, String kategori, String pathFile, String namaPemilik, String deskripsi) {
        super(idDokumen, judul, tanggalUpload, kategori, pathFile); // PEMANGGILAN KONSTRUKTOR DARI SUPERCLASS (INHERITANCE)
        this.namaPemilik = namaPemilik; // INISIALISASI NAMA PEMILIK
        this.deskripsi = deskripsi; // INISIALISASI DESKRIPSI
        this.conn = DatabaseConnection.connect(); // INISIALISASI KONEKSI JDBC
    }

    // GETTER DAN SETTER UNTUK NAMAPEMILIK DAN DESKRIPSI
    public String getNamaPemilik() {
        return namaPemilik; // MENGEMBALIKAN NAMA PEMILIK
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik; // MENGUBAH NILAI NAMA PEMILIK
    }

    public String getDeskripsi() {
        return deskripsi; // MENGEMBALIKAN DESKRIPSI
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi; // MENGUBAH NILAI DESKRIPSI
    }

    // IMPLEMENTASI METHOD ABSTRAK
    @Override
    public void tampilkanDetail() {
        System.out.println("=== Detail Dokumen Pribadi ==="); // MENAMPILKAN DETAIL DOKUMEN PRIBADI
        System.out.println("ID Dokumen      : " + idDokumen); // MENAMPILKAN ID DOKUMEN
        System.out.println("Judul           : " + judul); // MENAMPILKAN JUDUL DOKUMEN
        System.out.println("Tanggal Upload  : " + tanggalUpload); // MENAMPILKAN TANGGAL UPLOAD
        System.out.println("Kategori        : " + kategori); // MENAMPILKAN KATEGORI DOKUMEN
        System.out.println("Path File       : " + pathFile); // MENAMPILKAN PATH FILE DOKUMEN
        System.out.println("Nama Pemilik    : " + namaPemilik); // MENAMPILKAN NAMA PEMILIK DOKUMEN
        System.out.println("Deskripsi       : " + deskripsi); // MENAMPILKAN DESKRIPSI DOKUMEN
    }

    // MENAMBAHKAN DATA KE TABEL DOKUMEN_PRIBADI
    public void create(DokumenPribadi dokumenPribadi) {
        String sql = "INSERT INTO dokumen_pribadi (idDokumen, namaPemilik, deskripsi) VALUES (?, ?, ?)"; // QUERY UNTUK MENYIMPAN DATA (CRUD - CREATE)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, dokumenPribadi.getIdDokumen()); // SET NILAI ID DOKUMEN
            stmt.setString(2, dokumenPribadi.getNamaPemilik()); // SET NILAI NAMA PEMILIK
            stmt.setString(3, dokumenPribadi.getDeskripsi()); // SET NILAI DESKRIPSI
            stmt.executeUpdate(); // EKSEKUSI QUERY
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    // METODE UNTUK MEMUAT DATA DARI DATABASE
    public static DokumenPribadi loadFromDatabase(String id, String judul, Date tanggalUpload, String kategori, String pathFile, Connection conn) {
        String sql = "SELECT namaPemilik, deskripsi FROM dokumen_pribadi WHERE idDokumen = ?"; // QUERY UNTUK MEMUAT DATA (CRUD - READ)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY JDBC
            stmt.setString(1, id); // SET NILAI ID DOKUMEN
            ResultSet rs = stmt.executeQuery(); // EKSEKUSI QUERY DAN MENYIMPAN HASIL
            if (rs.next()) { // MEMERIKSA JIKA ADA DATA YANG DITEMUKAN
                String namaPemilik = rs.getString("namaPemilik"); // MENGAMBIL NILAI NAMA PEMILIK DARI HASIL QUERY
                String deskripsi = rs.getString("deskripsi"); // MENGAMBIL NILAI DESKRIPSI DARI HASIL QUERY
                return new DokumenPribadi(id, judul, tanggalUpload, kategori, pathFile, namaPemilik, deskripsi); // MENGEMBALIKAN OBJEK DOKUMENPRIBADI
            }
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
        return null; // MENGEMBALIKAN NILAI NULL JIKA DATA TIDAK DITEMUKAN
    }

    // GENERATE ID UNTUK DOKUMEN PRIBADI
    public static String generateId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // MEMBUAT FORMAT TIMESTAMP (MANIPULASI DATE)
        String timestamp = sdf.format(new Date()); // MENGAMBIL TIMESTAMP SAAT INI
        return "DP" + timestamp; // MENGEMBALIKAN ID DENGAN FORMAT "DP" DIDEPAN TIMESTAMP
    }
}
