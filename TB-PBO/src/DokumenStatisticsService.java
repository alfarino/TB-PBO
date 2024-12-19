import java.sql.*; // MENGIMPOR PAKET UNTUK PENGOPERASIAN DATABASE
import java.sql.Date; // DIGUNAKAN UNTUK MANIPULASI DATA TIPE TANGGAL
import java.util.*; // MENGIMPOR UTILITAS JAVA SEPERTI HASHMAP DAN LIST

public class DokumenStatisticsService {
    private Connection conn; // MENYIMPAN OBJEK CONNECTION UNTUK INTERAKSI DENGAN DATABASE

    // KONSTRUKTOR UNTUK MENERIMA KONEKSI DATABASE
    public DokumenStatisticsService(Connection conn) {
        this.conn = conn; // MENGINISIALISASI OBJEK CONNECTION
    }
 
    // METODE UNTUK MENGHITUNG JUMLAH DOKUMEN BERDASARKAN KATEGORI
    public Map<String, Integer> countDocumentsByCategory() {
        String sql = "SELECT kategori, COUNT(*) as jumlah FROM dokumen GROUP BY kategori"; // QUERY UNTUK MENGAMBIL DATA DOKUMEN PER KATEGORI
        Map<String, Integer> categoryCount = new HashMap<>(); // KOLEKSI UNTUK MENYIMPAN HASIL PENGHITUNGAN KATEGORI

        try (Statement stmt = conn.createStatement(); // MEMBUAT STATEMENT UNTUK EKSEKUSI QUERY
             ResultSet rs = stmt.executeQuery(sql)) { // MENJALANKAN QUERY DAN MENYIMPAN HASIL DI RESULTSET

            while (rs.next()) { // ITERASI MELALUI HASIL QUERY
                String kategori = rs.getString("kategori"); // MENGAMBIL KATEGORI DARI RESULTSET
                int jumlah = rs.getInt("jumlah"); // MENGAMBIL JUMLAH DARI RESULTSET
                categoryCount.put(kategori, jumlah); // MENAMBAHKAN HASIL KE MAP
            }

        } catch (SQLException e) { // MENANGANI EXCEPTION SAAT TERJADI KESALAHAN QUERY
            e.printStackTrace();
        }

        return categoryCount; // MENGEMBALIKAN HASIL DALAM BENTUK MAP
    }

    // METODE UNTUK MENGHITUNG JUMLAH DOKUMEN DALAM PERIODE TERTENTU
    public int countDocumentsByPeriod(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) as jumlah FROM dokumen WHERE tanggalUpload BETWEEN ? AND ?"; // QUERY UNTUK MENGAMBIL DATA BERDASARKAN PERIODE
        int count = 0; // VARIABEL UNTUK MENYIMPAN HASIL PENGHITUNGAN

        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // MEMBUAT PREPAREDSTATEMENT UNTUK QUERY PARAMETER
            stmt.setDate(1, new java.sql.Date(startDate.getTime())); // MENGATUR PARAMETER TANGGAL AWAL
            stmt.setDate(2, new java.sql.Date(endDate.getTime())); // MENGATUR PARAMETER TANGGAL AKHIR

            try (ResultSet rs = stmt.executeQuery()) { // MENJALANKAN QUERY DAN MENGAMBIL HASIL
                if (rs.next()) { // JIKA ADA HASIL
                    count = rs.getInt("jumlah"); // MENGAMBIL JUMLAH DARI HASIL QUERY
                }
            }

        } catch (SQLException e) { // MENANGANI EXCEPTION SAAT TERJADI KESALAHAN QUERY
            e.printStackTrace();
        }

        return count; // MENGEMBALIKAN HASIL JUMLAH DOKUMEN
    }

    // METODE UNTUK MENGHITUNG TOTAL PENDAPATAN DAN PENGELUARAN
    public Map<String, Double> calculateFinanceInsights() {
        Map<String, Double> financeInsights = new HashMap<>(); // KOLEKSI UNTUK MENYIMPAN DATA FINANSIAL
        financeInsights.put("Pendapatan", 0.0); // INISIALISASI TOTAL PENDAPATAN DENGAN 0.0
        financeInsights.put("Pengeluaran", 0.0); // INISIALISASI TOTAL PENGELUARAN DENGAN 0.0

        String query = "SELECT jenisTransaksi, jumlahTransaksi FROM laporan_keuangan"; // QUERY UNTUK MENGAMBIL DATA FINANSIAL
        try (PreparedStatement ps = conn.prepareStatement(query); // MEMBUAT PREPAREDSTATEMENT UNTUK EKSEKUSI QUERY
             ResultSet rs = ps.executeQuery()) { // MENJALANKAN QUERY DAN MENYIMPAN HASIL DI RESULTSET
            while (rs.next()) { // ITERASI MELALUI HASIL QUERY
                String jenisTransaksi = rs.getString("jenisTransaksi"); // MENGAMBIL TIPE TRANSAKSI DARI RESULTSET
                double jumlahTransaksi = rs.getDouble("jumlahTransaksi"); // MENGAMBIL JUMLAH TRANSAKSI DARI RESULTSET

                // MEMERIKSA JENIS TRANSAKSI DAN MENAMBAHKAN NILAI KE TOTAL YANG SESUAI
                if ("Pendapatan".equalsIgnoreCase(jenisTransaksi)) { 
                    financeInsights.put("Pendapatan", financeInsights.get("Pendapatan") + jumlahTransaksi);
                } else if ("Pengeluaran".equalsIgnoreCase(jenisTransaksi)) {
                    financeInsights.put("Pengeluaran", financeInsights.get("Pengeluaran") + jumlahTransaksi);
                }
            }
        } catch (SQLException e) { // MENANGANI EXCEPTION SAAT TERJADI KESALAHAN QUERY
            e.printStackTrace();
        }
        return financeInsights; // MENGEMBALIKAN HASIL DALAM BENTUK MAP
    }
}
