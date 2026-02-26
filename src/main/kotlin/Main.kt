import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HomeSyncApplication

fun main(args: Array<String>) {
    runApplication<HomeSyncApplication>(*args)
}
