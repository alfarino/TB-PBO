// KELAS UTAMA: DOKUMEN
// BERPERAN SEBAGAI CLASS INDUK
import java.util.Date; // PENERAPAN PEMBELAJARAN DATE UNTUK MEREKAM TANGGAL PENYIMPANAN DOKUMEN

public abstract class Dokumen { // CLASS ABSTRAK DOKUMEN
    // ATRIBUT DARI CLASS DOKUMEN
    protected String idDokumen; // ID DOKUMEN UNTUK IDENTITAS
    protected String judul; // JUDUL DOKUMEN
    protected Date tanggalUpload; // TANGGAL UPLOAD MENGGUNAKAN OBJECT DATE (MANIPULASI DATE)
    protected String kategori; // KATEGORI DOKUMEN
    protected String pathFile; // PATH FILE UNTUK MENYIMPAN LOKASI FILE DOKUMEN

    // KONSTRUKTOR
    public Dokumen(String idDokumen, String judul, Date tanggalUpload, String kategori, String pathFile) {
        this.idDokumen = idDokumen; // INISIALISASI ID DOKUMEN
        this.judul = judul; // INISIALISASI JUDUL DOKUMEN
        this.tanggalUpload = tanggalUpload; // INISIALISASI TANGGAL UPLOAD (MANIPULASI DATE)
        this.kategori = kategori; // INISIALISASI KATEGORI
        this.pathFile = pathFile; // INISIALISASI PATH FILE
    }

    // GETTER DAN SETTER DARI CLASS DOKUMEN
    public String getIdDokumen() {
        return idDokumen; // MENGEMBALIKAN ID DOKUMEN
    }

    public void setIdDokumen(String idDokumen) {
        this.idDokumen = idDokumen; // MENGUBAH NILAI ID DOKUMEN
    }

    public String getJudul() {
        return judul; // MENGEMBALIKAN JUDUL DOKUMEN
    }

    public void setJudul(String judul) {
        this.judul = judul; // MENGUBAH NILAI JUDUL DOKUMEN
    }

    public Date getTanggalUpload() {
        return tanggalUpload; // MENGEMBALIKAN TANGGAL UPLOAD (MANIPULASI DATE)
    }

    public void setTanggalUpload(Date tanggalUpload) {
        this.tanggalUpload = tanggalUpload; // MENGUBAH NILAI TANGGAL UPLOAD (MANIPULASI DATE)
    }

    public String getKategori() {
        return kategori; // MENGEMBALIKAN KATEGORI DOKUMEN
    }

    public void setKategori(String kategori) {
        this.kategori = kategori; // MENGUBAH NILAI KATEGORI
    }

    public String getPathFile() {
        return pathFile; // MENGEMBALIKAN PATH FILE DOKUMEN
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile; // MENGUBAH NILAI PATH FILE
    }

    // METHOD ABSTRAK UNTUK DITURUNKAN
    public abstract void tampilkanDetail(); // METHOD ABSTRAK YANG AKAN DIDEFINISIKAN DI SUB CLASS
}
