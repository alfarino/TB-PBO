// Interface: DokumenService // PENERAPAN NOMOR 2 INTERFACE, IMPLEMENTASI DI CLASS DokumenServiceImpl
import java.util.List;

// Interface DokumenService mendefinisikan kontrak untuk operasi-operasi dasar yang dapat dilakukan pada dokumen
public interface DokumenService {

    // Menyimpan dokumen baru
    void create(Dokumen dokumen); // PENERAPAN NOMOR 2 INTERFACE: MENDEFINISIKAN METODE UNTUK MENCIPTAKAN DOKUMEN

    // Mengambil daftar semua dokumen yang ada
    List<Dokumen> read(); // PENERAPAN NOMOR 7 COLLECTION FRAMEWORK: MENGGUNAKAN LIST UNTUK MENYIMPAN DAN MENGEMBALIKAN KOLEKSI DOKUMEN

    // Memperbarui informasi dokumen yang ada berdasarkan ID
    void update(String id, Dokumen dokumen); // 2. INTERFACE: MENDEFINISIKAN METODE UNTUK MEMPERBARUI DOKUMEN

    // Menghapus dokumen berdasarkan ID
    void delete(String id); // 2. INTERFACE: MENDEFINISIKAN METODE UNTUK MENGHAPUS DOKUMEN
}
