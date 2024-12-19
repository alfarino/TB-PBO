import java.sql.*; // IMPORT UNTUK KONEKSI DATABASE JDBC
import java.util.*; // IMPORT UNTUK COLECTION FRAMEWORK (ARRAYLIST) PENERAPAN NOMOR 7
import java.util.Date; // IMPORT UNTUK MANIPULASI TANGGAL

public class DokumenServiceImpl implements DokumenService { // IMPLEMENTASI INTERFACE DOKUMENSERVICE
    private Connection conn; // KONEKSI DATABASE UNTUK INTERAKSI DENGAN DB

    // KONSTRUKTOR
    public DokumenServiceImpl() {
        this.conn = DatabaseConnection.connect(); // MENGINISIALISASI KONEKSI KE DATABASE
    }

    @Override
    public void create(Dokumen dokumen) {
        String sql = "INSERT INTO dokumen (idDokumen, judul, tanggalUpload, kategori, pathFile) VALUES (?, ?, ?, ?, ?)"; // QUERY UNTUK MENYIMPAN DOKUMEN KE DATABASE
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK MENGHINDARI SQL INJECTION
            stmt.setString(1, dokumen.getIdDokumen()); // SET ID DOKUMEN
            stmt.setString(2, dokumen.getJudul()); // SET JUDUL DOKUMEN
            stmt.setDate(3, new java.sql.Date(dokumen.getTanggalUpload().getTime())); // MANIPULASI METHOD DATE UNTUK MENGUBAH DATE JAVA MENJADI SQL DATE
            stmt.setString(4, dokumen.getKategori()); // SET KATEGORI DOKUMEN
            stmt.setString(5, dokumen.getPathFile()); // SET PATH FILE DOKUMEN
            stmt.executeUpdate(); // MENYALIN DOKUMEN KE DATABASE

            // PANGGIL METODE CREATE DARI SUBCLASS JIKA DOKUMEN ADALAH INSTANSI KHUSUS
            if (dokumen instanceof DokumenLegal) { // CEK JIKA DOKUMEN ADALAH INSTANSI DARI KELAS DOKUMENLEGAL
                ((DokumenLegal) dokumen).create((DokumenLegal) dokumen); // PANGGIL METODE CREATE DARI DOKUMENLEGAL
            } else if (dokumen instanceof LaporanKeuangan) { // CEK JIKA DOKUMEN ADALAH INSTANSI DARI KELAS LAPORANKEUANGAN
                ((LaporanKeuangan) dokumen).create((LaporanKeuangan) dokumen); // PANGGIL METODE CREATE DARI LAPORANKEUANGAN
            } else if (dokumen instanceof SuratMenyurat) { // CEK JIKA DOKUMEN ADALAH INSTANSI DARI KELAS SURATMENYURAT
                ((SuratMenyurat) dokumen).create((SuratMenyurat) dokumen); // PANGGIL METODE CREATE DARI SURATMENYURAT
            } else if (dokumen instanceof DokumenPribadi) { // CEK JIKA DOKUMEN ADALAH INSTANSI DARI KELAS DOKUMENPRIBADI
                ((DokumenPribadi) dokumen).create((DokumenPribadi) dokumen); // PANGGIL METODE CREATE DARI DOKUMENPRIBADI
            }
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            System.err.println("Kesalahan SQL saat menyimpan dokumen: " + e.getMessage()); // MENAMPILKAN PESAN ERROR JIKA TERJADI
        }
    }

    @Override
    public List<Dokumen> read() {
        List<Dokumen> dokumenList = new ArrayList<>();  // 7. COLLECTION FRAMEWORK: MENYIMPAN DOKUMEN YANG DIBACA DARI DATABASE
    
        // QUERY UNTUK MEMBACA DATA DARI DATABASE DENGAN LEFT JOIN ANTAR TABEL
        String sql = """
            SELECT d.idDokumen, d.judul, d.tanggalUpload, d.kategori, d.pathFile,
                   l.nomorKontrak, 
                   k.jumlahTransaksi, k.jenisTransaksi,
                   s.nomorSurat, s.penerima,
                   p.namaPemilik, p.deskripsi
            FROM dokumen d
            LEFT JOIN dokumen_legal l ON d.idDokumen = l.idDokumen AND d.kategori = 'Legal'
            LEFT JOIN laporan_keuangan k ON d.idDokumen = k.idDokumen AND d.kategori = 'Keuangan'
            LEFT JOIN surat_menyurat s ON d.idDokumen = s.idDokumen AND d.kategori = 'Surat'
            LEFT JOIN dokumen_pribadi p ON d.idDokumen = p.idDokumen AND d.kategori = 'Pribadi'
        """;
    
        try (Statement stmt = conn.createStatement(); // MENGGUNAKAN STATEMENT UNTUK MENJALANKAN QUERY
             ResultSet rs = stmt.executeQuery(sql)) { // MENYIMPAN HASIL QUERY DALAM RESULTSET
    
            while (rs.next()) { // LOOPING UNTUK MEMBACA SETIAP DATA DARI RESULTSET
                String id = rs.getString("idDokumen"); // MENGAMBIL ID DOKUMEN
                String judul = rs.getString("judul"); // MENGAMBIL JUDUL DOKUMEN
                Date tanggalUpload = rs.getDate("tanggalUpload"); // MENGAMBIL TANGGAL UPLOAD
                String kategori = rs.getString("kategori"); // MENGAMBIL KATEGORI DOKUMEN
                String pathFile = rs.getString("pathFile"); // MENGAMBIL PATH FILE DOKUMEN
    
                Dokumen dokumen = null; // INISIALISASI OBJEK DOKUMEN
    
                // PENGECEKAN BERDASARKAN KATEGORI DOKUMEN
                switch (kategori) { 
                    case "Legal": // UNTUK KATEGORI LEGAL
                        String nomorKontrak = rs.getString("nomorKontrak"); // MENGAMBIL NOMOR KONTRAK DARI RESULTSET
                        dokumen = new DokumenLegal(id, judul, tanggalUpload, kategori, pathFile, nomorKontrak); // INSTANSI DOKUMENLEGAL
                        break;
                    case "Keuangan": // UNTUK KATEGORI KEUANGAN
                        double jumlahTransaksi = rs.getDouble("jumlahTransaksi"); // MENGAMBIL JUMLAH TRANSAKSI DARI RESULTSET
                        String jenisTransaksi = rs.getString("jenisTransaksi"); // MENGAMBIL JENIS TRANSAKSI DARI RESULTSET
                        dokumen = new LaporanKeuangan(id, judul, tanggalUpload, kategori, pathFile, jumlahTransaksi, jenisTransaksi); // INSTANSI LAPORANKEUANGAN
                        break;
                    case "Surat": // UNTUK KATEGORI SURAT
                        String nomorSurat = rs.getString("nomorSurat"); // MENGAMBIL NOMOR SURAT DARI RESULTSET
                        String penerima = rs.getString("penerima"); // MENGAMBIL PENERIMA DARI RESULTSET
                        dokumen = new SuratMenyurat(id, judul, tanggalUpload, kategori, pathFile, nomorSurat, penerima); // INSTANSI SURATMENYURAT
                        break;
                    case "Pribadi": // UNTUK KATEGORI PRIBADI
                        String namaPemilik = rs.getString("namaPemilik"); // MENGAMBIL NAMA PEMILIK DARI RESULTSET
                        String deskripsi = rs.getString("deskripsi"); // MENGAMBIL DESKRIPSI DARI RESULTSET
                        dokumen = new DokumenPribadi(id, judul, tanggalUpload, kategori, pathFile, namaPemilik, deskripsi); // INSTANSI DOKUMENPRIBADI
                        break;
                    default:
                        System.out.println("Kategori tidak dikenali: " + kategori); // PESAN ERROR JIKA KATEGORI TIDAK DIKENALI
                }
    
                if (dokumen != null) { // MENAMBAHKAN DOKUMEN KE LIST JIKA OBJEK DOKUMEN BERHASIL DIBUAT
                    dokumenList.add(dokumen);
                }
            }
    
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    
        return dokumenList; // MENGEMBALIKAN LIST DOKUMEN
    }

    @Override
    public void update(String id, Dokumen dokumen) {
        String sql = "UPDATE dokumen SET judul = ?, tanggalUpload = ?, kategori = ?, pathFile = ? WHERE idDokumen = ?"; // QUERY UNTUK UPDATE DOKUMEN
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY
            stmt.setString(1, dokumen.getJudul()); // SET JUDUL DOKUMEN
            stmt.setDate(2, new java.sql.Date(dokumen.getTanggalUpload().getTime())); // MANIPULASI METHOD DATE UNTUK MENGUBAH DATE JAVA MENJADI SQL DATE
            stmt.setString(3, dokumen.getKategori()); // SET KATEGORI DOKUMEN
            stmt.setString(4, dokumen.getPathFile()); // SET PATH FILE DOKUMEN
            stmt.setString(5, id); // SET ID DOKUMEN
            stmt.executeUpdate(); // EKSEKUSI QUERY UNTUK UPDATE
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM dokumen WHERE idDokumen = ?"; // QUERY UNTUK MENGHAPUS DOKUMEN
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PENGGUNAAN PREPAREDSTATEMENT UNTUK QUERY
            stmt.setString(1, id); // SET ID DOKUMEN
            stmt.executeUpdate(); // EKSEKUSI QUERY UNTUK MENGHAPUS
        } catch (SQLException e) { // EXCEPTION HANDLING UNTUK JDBC
            e.printStackTrace(); // MENAMPILKAN ERROR JIKA TERJADI
        }
    }

    // MENUTUP KONEKSI SAAT APLIKASI SELESAI
    public void close() {
        DatabaseConnection.disconnect(conn); // MENUTUP KONEKSI DATABASE
    }

    public Connection getConnection() {
        return conn; // Mengembalikan objek koneksi database
    }
    
}
