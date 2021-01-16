package com.init.shortink.controller

import com.init.shortink.model.Link
import com.init.shortink.repo.LinkRepo
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.ws.rs.Path


@CrossOrigin
@RestController
@RequestMapping("/api")
class LinkController(
        val linkRepo: LinkRepo
) {

    private final var encodedImg: String = ""

    init {
        WebDriverManager.chromedriver().setup()
    }

    private fun getPreviewImg(url: String) {
        val driver: ChromeDriver = ChromeDriver()
        val options: ChromeOptions = ChromeOptions()

        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36 OPR/60.0.3255.170")
        options.setHeadless(true)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("window-size=1920,1080")
        driver.get(url)

        encodedImg = driver.getScreenshotAs(OutputType.BASE64)

        driver.quit()
    }

    @PutMapping("/link/{id}/update-img")
    private fun updateImg(@PathVariable id: Long, @RequestBody link: Link): ResponseEntity<Link> {
        val driver: ChromeDriver = ChromeDriver()
        val options: ChromeOptions = ChromeOptions()

        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
        driver.get(link.nativeLink)

        val curLink: Link = linkRepo.getOne(id)
        curLink.previewImg = driver.getScreenshotAs(OutputType.BASE64)

        linkRepo.save(curLink)

        driver.quit()

        return ResponseEntity.ok(curLink)
    }

    @GetMapping("/link/all")
    private fun getAll() : ResponseEntity<List<Link>> {
        return ResponseEntity.ok(linkRepo.findAll())
    }

    @PostMapping("/link")
    private fun createLink(@RequestBody link: Link) : ResponseEntity<Any> {
        try {
            if (link.encryptLink.isEmpty() || link.nativeLink.isEmpty()) {
                throw Exception("Empty fields")
            }

            if (link.user !== null) {
                val checkExists: Long = linkRepo.checkUnique(link.encryptLink, link.user!!.id).toArray().size.toLong()
                if (checkExists > 0L) {
                    throw Exception("Duplicates find!")
                }
            }

            val dateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss")

            getPreviewImg(link.nativeLink)

            link.previewImg =
                if (encodedImg.isNotEmpty())
                    encodedImg
                else
                    null.toString()
            link.createdDate = dateTime.format(formatter)
            linkRepo.save(link)

            return ResponseEntity.ok(linkRepo.getOne(link.id))
        } catch (e: Exception) {
            val info: HashMap<String, String> = HashMap()
            info["status"] = "404"
            info["msg"] = "${e.message}"

            return ResponseEntity(info, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/link/{id}")
    private fun deleteLink(@RequestBody id: Long) : ResponseEntity<Any> {
        try {
            val link: Link = linkRepo.getOne(id)
            if (link !== null) {
                linkRepo.delete(link)
            } else {
                throw Exception()
            }
            return ResponseEntity.ok("Ссылка была удалена")
        } catch (e: Exception) {
            return ResponseEntity("Ссылка не была найдена", HttpStatus.BAD_REQUEST)
        }
    }
}