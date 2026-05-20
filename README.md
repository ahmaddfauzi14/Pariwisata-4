# Pariwisata-4

Cara Kontribusi Di Projek Ini:
1. Fork repo ini ke githubmu
2. Clone repo yang telah di fork ke laptopmu
3. Buka project github nya di code editor (VSCode) -> buka terminal
4. Ketik "git checkout (nama_branch_yang_telah_kubuat)" untuk ganti ke branch punya yang telah dibuat, misalnya "git checkout Fauzi"
5. Setiap perubahan dalam code akan di push ke masing-masing branch milik kita, tidak langsung ke branch utama (main), baru lakukan pull request ke branch utama (main) yang dimana akan dicek terlebih dahulu kode yang telah kita buat baru digabungkan ke branch utama (main). Tujuannya agar tidak menimpa kode kita 
6. Selalu sync kode kita dengan mengetikkan:
  git checkout main
  git pull origin main
  git checkout <nama-branch-anda>
  git merge main
