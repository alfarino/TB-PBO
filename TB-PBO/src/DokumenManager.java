import java.util.*; // MENGIMPORT LIBRARY UNTUK MENDUKUNG PENGGUNAAN KOLEKSI DAN SCANNER

// Class yang dipanggil melalui class main, menjadi tempat utama dimana semua kelas dioperasikan
public class DokumenManager {

    // Membuat instance dari DokumenService menggunakan implementasi DokumenServiceImpl
    private static DokumenService dokumenService = new DokumenServiceImpl(); // INISIALISASI DOKUMENSERVICE UNTUK MENGHUBUNGKAN LAYANAN DOKUMEN

    private static DokumenStatisticsService dokumenStatisticsService = new DokumenStatisticsService(((DokumenServiceImpl) dokumenService).getConnection());

    // Variabel Scanner dibuat sekali untuk seluruh kelas
    private static final Scanner scanner = new Scanner(System.in);

    // Metode untuk menampilkan menu utama
    public static void tampilkanMenu() {
        try (Scanner scanner = new Scanner(System.in)) { // MENGGUNAKAN SCANNER UNTUK MENGAMBIL INPUT DARI PENGGUNA
            while (true) { // LOOP UNTUK TERUS MENAMPILKAN MENU SAMPAI PENGGUNA MEMILIH KELUAR
                System.out.println("\n=== Menu ==="); // MENAMPILKAN HEADER MENU
                System.out.println("1. Tambah Dokumen"); // Opsi 1 UNTUK MENAMBAH DOKUMEN
                System.out.println("2. Lihat Dokumen"); // Opsi 2 UNTUK MELIHAT DOKUMEN
                System.out.println("3. Update Dokumen"); // Opsi 3 UNTUK MEMPERBARUI DOKUMEN
                System.out.println("4. Hapus Dokumen"); // Opsi 4 UNTUK MENGHAPUS DOKUMEN
                System.out.println("5. Keluar"); // Opsi 5 UNTUK KELUAR DARI APLIKASI
                System.out.println("6. Tampilkan Statistik Dokumen"); // Tambahkan opsi statistik
                System.out.print("Pilih opsi: "); // MEMINTA INPUT PILIHAN DARI PENGGUNA

                if (!scanner.hasNextInt()) { // Periksa apakah input adalah integer
                    System.out.println("Harap masukkan angka.");
                    scanner.next(); // Bersihkan buffer scanner
                    continue; // Ulangi loop
                }

                int pilihan = scanner.nextInt(); // MEMBACA INPUT PILIHAN SEBAGAI INTEGER
                scanner.nextLine(); // MENANGANI NEWLINE SETELAH INPUT INTEGER

                // Switch case untuk menangani setiap pilihan menu
                switch (pilihan) {
                    case 1: // Menambah dokumen
                        System.out.println("Masukkan Judul:"); // MEMINTA JUDUL DOKUMEN
                        String judul = scanner.nextLine(); // MEMBACA INPUT JUDUL
                        System.out.println("Masukkan Kategori (Legal/Keuangan/Surat/Pribadi):"); // MEMINTA KATEGORI DOKUMEN
                        String kategori = scanner.nextLine(); // MEMBACA INPUT KATEGORI
                        System.out.println("Masukkan Path File:"); // MEMINTA PATH FILE
                        String pathFile = scanner.nextLine(); // MEMBACA INPUT PATH FILE

                        // Mendapatkan tanggal saat ini
                        Date tanggalUpload = new Date(); // MEMBUAT INSTANSI TANGGAL SAAT INI

                        // Switch case untuk menangani kategori dokumen
                        switch (kategori.toLowerCase()) { // 5. MANIPULASI METHOD STRING // MENORMALISASI INPUT KATEGORI KE HURUF KECIL
                            case "legal": // Jika kategori adalah legal
                                System.out.println("Masukkan Nomor Kontrak:"); // MEMINTA NOMOR KONTRAK
                                String nomorKontrak = scanner.nextLine(); // MEMBACA INPUT NOMOR KONTRAK

                                // Membuat instance DokumenLegal dengan ID otomatis
                                DokumenLegal dokumenLegal = new DokumenLegal(
                                    DokumenLegal.generateId(), // MEMANGGIL METODE GENERATEID UNTUK MEMBUAT ID
                                    judul, // JUDUL DOKUMEN
                                    tanggalUpload, // TANGGAL UPLOAD
                                    kategori, // KATEGORI DOKUMEN
                                    pathFile, // PATH FILE DOKUMEN
                                    nomorKontrak // NOMOR KONTRAK
                                );

                                // Menyimpan dokumen ke database
                                dokumenService.create(dokumenLegal); // MEMANGGIL METODE CREATE PADA SERVICE
                                System.out.println("Dokumen Legal berhasil ditambahkan."); // PESAN SUKSES
                                break;

                            case "keuangan": // Jika kategori adalah keuangan
                                System.out.println("Masukkan Jumlah Transaksi:"); // MEMINTA JUMLAH TRANSAKSI
                                double jumlahTransaksi = scanner.nextDouble(); // MEMBACA INPUT JUMLAH TRANSAKSI
                                scanner.nextLine(); // KONSUMSI NEWLINE
                                System.out.println("Masukkan Jenis Transaksi (Pendapatan/Pengeluaran):"); // MEMINTA JENIS TRANSAKSI
                                String jenisTransaksi = scanner.nextLine(); // MEMBACA INPUT JENIS TRANSAKSI

                                // Membuat instance LaporanKeuangan dengan ID otomatis
                                LaporanKeuangan laporanKeuangan = new LaporanKeuangan(
                                    LaporanKeuangan.generateId(), // MEMANGGIL METODE GENERATEID UNTUK MEMBUAT ID
                                    judul, // JUDUL DOKUMEN
                                    tanggalUpload, // TANGGAL UPLOAD
                                    kategori, // KATEGORI DOKUMEN
                                    pathFile, // PATH FILE DOKUMEN
                                    jumlahTransaksi, // JUMLAH TRANSAKSI
                                    jenisTransaksi // JENIS TRANSAKSI
                                );

                                // Menyimpan dokumen ke database
                                dokumenService.create(laporanKeuangan); // MEMANGGIL METODE CREATE PADA SERVICE
                                System.out.println("Laporan Keuangan berhasil ditambahkan."); // PESAN SUKSES
                                break;

                            case "surat": // Jika kategori adalah surat
                                System.out.println("Masukkan Nomor Surat:"); // MEMINTA NOMOR SURAT
                                String nomorSurat = scanner.nextLine(); // MEMBACA INPUT NOMOR SURAT
                                System.out.println("Masukkan Penerima:"); // MEMINTA PENERIMA
                                String penerima = scanner.nextLine(); // MEMBACA INPUT PENERIMA

                                // Membuat instance SuratMenyurat dengan ID otomatis
                                SuratMenyurat suratMenyurat = new SuratMenyurat(
                                    SuratMenyurat.generateId(), // MEMANGGIL METODE GENERATEID UNTUK MEMBUAT ID
                                    judul, // JUDUL DOKUMEN
                                    tanggalUpload, // TANGGAL UPLOAD
                                    kategori, // KATEGORI DOKUMEN
                                    pathFile, // PATH FILE DOKUMEN
                                    nomorSurat, // NOMOR SURAT
                                    penerima // PENERIMA SURAT
                                );

                                // Menyimpan dokumen ke database
                                dokumenService.create(suratMenyurat); // MEMANGGIL METODE CREATE PADA SERVICE
                                System.out.println("Surat Menyurat berhasil ditambahkan."); // PESAN SUKSES
                                break;

                            case "pribadi": // Jika kategori adalah pribadi
                                System.out.println("Masukkan Nama Pemilik:"); // MEMINTA NAMA PEMILIK
                                String namaPemilik = scanner.nextLine(); // MEMBACA INPUT NAMA PEMILIK
                                System.out.println("Masukkan Deskripsi:"); // MEMINTA DESKRIPSI
                                String deskripsi = scanner.nextLine(); // MEMBACA INPUT DESKRIPSI

                                // Membuat instance DokumenPribadi dengan ID otomatis
                                DokumenPribadi dokumenPribadi = new DokumenPribadi(
                                    DokumenPribadi.generateId(), // MEMANGGIL METODE GENERATEID UNTUK MEMBUAT ID
                                    judul, // JUDUL DOKUMEN
                                    tanggalUpload, // TANGGAL UPLOAD
                                    kategori, // KATEGORI DOKUMEN
                                    pathFile, // PATH FILE DOKUMEN
                                    namaPemilik, // NAMA PEMILIK
                                    deskripsi // DESKRIPSI DOKUMEN
                                );

                                // Menyimpan dokumen ke database
                                dokumenService.create(dokumenPribadi); // MEMANGGIL METODE CREATE PADA SERVICE
                                System.out.println("Dokumen Pribadi berhasil ditambahkan."); // PESAN SUKSES
                                break;

                            default: // Jika kategori tidak valid
                                System.out.println("Kategori tidak valid."); // PESAN KESALAHAN UNTUK KATEGORI TIDAK VALID
                                break;
                        }
                        break;

                    case 2: // Melihat dokumen
                        List<Dokumen> dokumenList = dokumenService.read(); // MEMBACA DAFTAR DOKUMEN DARI DATABASE
                        if (dokumenList.isEmpty()) { // JIKA TIDAK ADA DOKUMEN
                            System.out.println("Tidak ada dokumen yang tersedia."); // PESAN TIDAK ADA DOKUMEN
                        } else { // JIKA ADA DOKUMEN
                            for (Dokumen d : dokumenList) { // LOOP UNTUK SETIAP DOKUMEN
                                d.tampilkanDetail(); // MENAMPILKAN DETAIL DOKUMEN
                                System.out.println(); // MENAMBAHKAN BARIS KOSONG
                            }
                        }
                        break;

                    case 3: // Memperbarui dokumen
                        System.out.println("Masukkan ID Dokumen yang ingin diupdate:"); // MEMINTA ID DOKUMEN
                        String updateId = scanner.nextLine(); // MEMBACA INPUT ID DOKUMEN
                        System.out.println("Masukkan Judul baru:"); // MEMINTA JUDUL BARU
                        String updateJudul = scanner.nextLine(); // MEMBACA INPUT JUDUL BARU
                        System.out.println("Masukkan Path File baru:"); // MEMINTA PATH FILE BARU
                        String updatePathFile = scanner.nextLine(); // MEMBACA INPUT PATH FILE BARU

                        // Membuat instance dokumen untuk diperbarui
                        Dokumen updateDokumen = new DokumenPribadi(updateId, updateJudul, new Date(), "Pribadi", updatePathFile, "", "");
                        dokumenService.update(updateId, updateDokumen); // MEMANGGIL METODE UPDATE PADA SERVICE
                        System.out.println("Dokumen berhasil diperbarui."); // PESAN SUKSES
                        break;

                    case 4: // Menghapus dokumen
                        System.out.println("Masukkan ID Dokumen yang ingin dihapus:"); // MEMINTA ID DOKUMEN
                        String deleteId = scanner.nextLine(); // MEMBACA INPUT ID DOKUMEN
                        dokumenService.delete(deleteId); // MEMANGGIL METODE DELETE PADA SERVICE
                        System.out.println("Dokumen berhasil dihapus."); // PESAN SUKSES
                        break;

                    case 5: // Keluar dari program
                        System.exit(0); // MENGHENTIKAN PROGRAM
                        break;
                    
                    case 6: // Menampilkan statistik dokumen
                        tampilkanStatistik();
                        break;

                    default: // Jika pilihan tidak valid
                        System.out.println("Pilihan tidak valid."); // PESAN KESALAHAN UNTUK PILIHAN TIDAK VALID
                }
            }
        } 
    }

    private static void tampilkanStatistik() {
        // Menampilkan jumlah dokumen berdasarkan kategori
        System.out.println("\n=== Statistik Dokumen ===");
        Map<String, Integer> categoryStats = dokumenStatisticsService.countDocumentsByCategory();
        System.out.println("Jumlah Dokumen Berdasarkan Kategori:");
        for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Statistik keuangan
        System.out.println("\n=== Insight Laporan Keuangan ===");
        Map<String, Double> financeInsights = dokumenStatisticsService.calculateFinanceInsights();
        System.out.println("Berikut insight laporan keuangan Anda:");
        System.out.println("Total Pendapatan: Rp" + financeInsights.get("Pendapatan"));
        System.out.println("Total Pengeluaran: Rp" + financeInsights.get("Pengeluaran"));
        System.out.println("==================================");
    
            System.out.println("\nMasukkan Tanggal Awal (YYYY-MM-DD):");
            String startInput = scanner.nextLine();
            java.sql.Date startDate;
            try {
                startDate = java.sql.Date.valueOf(startInput); // Konversi String ke java.sql.Date
            } catch (IllegalArgumentException e) {
                System.out.println("Format tanggal awal salah. Harap gunakan format YYYY-MM-DD.");
                return; // Keluar dari metode
            }
        
            System.out.println("Masukkan Tanggal Akhir (YYYY-MM-DD):");
            String endInput = scanner.nextLine();
            java.sql.Date endDate;
            try {
                endDate = java.sql.Date.valueOf(endInput); // Konversi String ke java.sql.Date
            } catch (IllegalArgumentException e) {
                System.out.println("Format tanggal akhir salah. Harap gunakan format YYYY-MM-DD.");
                return; // Keluar dari metode
            }
        
            // Proses statistik
            int count = dokumenStatisticsService.countDocumentsByPeriod(startDate, endDate);
            System.out.println("Jumlah Dokumen diunggah antara " + startDate + " dan " + endDate + ": " + count);
        
        
    }
    
    // Metode utama untuk menjalankan program
    public static void main(String[] args) {
        tampilkanMenu(); // MEMANGGIL METODE TAMPILKANMENU
    }


}
