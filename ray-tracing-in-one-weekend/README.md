
# Ray Tracing in One Weekend in Kotlin

This is a direkt C++ to Kotlin translation from the Books on [Ray Tracing](https://raytracing.github.io/) with minimal changes.
This is most probably not good Kotlin code.


## Notes on optimization

Using in vec3 direct x, y, z fields instead of e: 370 s -> 199 s.

Changing Double -> Float: slower, 199 s -> 218 s, reverted.


# License

As my own contribution apart from the translation is low the same license applies.


## Sources

[_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html)

[_Ray Tracing: The Next Week_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)

TODO: [_Ray Tracing: The Rest of Your Life_](https://raytracing.github.io/books/RayTracingTheRestOfYourLife.html)


# Show off

The expected images are in the original book. Here are the somehow funny implementation/translation errors. Guess the error or look in the alt text. 

![wrong calculcation of the pattern](images/image1624114594948.png?raw=true "asdad")
![missed the instruction about comment out](images/image1624126052168.png?raw=true "asdad")
![Use origin() and direction() directly instead of a copy](images/image1624192388685.png?raw=true "asdad")
![sign error for inverted density](images/image1624345263532.png?raw=true "asdad")
![obvious wrong, was a combination of the previous errors](images/image1624220234602.png?raw=true "asdad")
