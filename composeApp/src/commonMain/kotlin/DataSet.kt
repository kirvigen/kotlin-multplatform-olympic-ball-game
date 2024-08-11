import ballsgame.composeapp.generated.resources.Res
import ballsgame.composeapp.generated.resources.*
import screen.game.data.BallType

object DataSet {
    val olympicSummerCities = listOf(
        BallType(
            image = Res.drawable.paris,
            name = "Paris",
            mass = 512,
            additionalInformation = "in 2024"
        ),
        BallType(
            image = Res.drawable.tokyo,
            name = "Tokyo",
            mass = 256,
            additionalInformation = "in 2020 (held in 2021)"
        ),
        BallType(
            image = Res.drawable.rio,
            name = "Rio de Janeiro",
            mass = 128,
            additionalInformation = "in 2016"
        ),
        BallType(
            image = Res.drawable.london,
            name = "London",
            mass = 64,
            additionalInformation = "in 2012"
        ),
        BallType(
            image = Res.drawable.beijing,
            name = "Beijing",
            mass = 32,
            additionalInformation = "in 2008"
        ),
        BallType(
            image = Res.drawable.athens,
            name = "Athens",
            mass = 16,
            additionalInformation = "in 2004"
        ),
        BallType(
            image = Res.drawable.sydney,
            name = "Sydney",
            mass = 8,
            additionalInformation = "in 2000"
        ),
        BallType(
            image = Res.drawable.atlanta,
            name = "Atlanta",
            mass = 4,
            additionalInformation = "in 1996"
        ),
        BallType(
            image = Res.drawable.barcelona,
            name = "Barcelona",
            mass = 2,
            additionalInformation = "in 1992"
        ),
        BallType(
            image = Res.drawable.los_angeles,
            name = "Los Angeles",
            mass = 1,
            additionalInformation = "in 1984"
        )
    )
    val famousAthletes = listOf(
        BallType(
            image = Res.drawable.lionel_messi,
            name = "Lionel Messi",
            mass = 512,
            additionalInformation = "Football, 7-time Ballon d'Or Winner"
        ),
        BallType(
            image = Res.drawable.serena_williams,
            name = "Serena Williams",
            mass = 256,
            additionalInformation = "Tennis, 23-time Grand Slam Champion"
        ),
        BallType(
            image = Res.drawable.usain_bolt,
            name = "Usain Bolt",
            mass = 128,
            additionalInformation = "Athletics, 8-time Olympic Gold Medalist"
        ),
        BallType(
            image = Res.drawable.michael_phelps,
            name = "Michael Phelps",
            mass = 64,
            additionalInformation = "Swimming, 23-time Olympic Gold Medalist"
        ),
        BallType(
            image = Res.drawable.lebron_james,
            name = "LeBron James",
            mass = 32,
            additionalInformation = "Basketball, 4-time NBA Champion"
        ),
        BallType(
            image = Res.drawable.roger_federer,
            name = "Roger Federer",
            mass = 16,
            additionalInformation = "Tennis, 20-time Grand Slam Champion"
        ),
        BallType(
            image = Res.drawable.simone_biles,
            name = "Simone Biles",
            mass = 8,
            additionalInformation = "Gymnastics, 7-time Olympic Medalist"
        ),
        BallType(
            image = Res.drawable.muhammad_ali,
            name = "Muhammad Ali",
            mass = 4,
            additionalInformation = "Boxing, 3-time World Heavyweight Champion"
        ),
        BallType(
            image = Res.drawable.david_beckham,
            name = "David Beckham",
            mass = 2,
            additionalInformation = "Football, Iconic Footballer, MLS Champion"
        ),
        BallType(
            image = Res.drawable.tom_daley,
            name = "Tom Daley",
            mass = 1,
            additionalInformation = "Diver from the UK. Gold medal at the Tokyo 2020 Olympics"
        )
    )
}