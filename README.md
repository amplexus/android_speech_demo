# android_voice_demo

Android app to demonstrate use of Google Voice in conjunction with Apache OpenNLP text classification library for interpreting speech. The idea being that you can use this app to issue commands to a voice activated robot.

To give an example, lets say a hypothetical voice activated robot understands these commands: left, right, forward, backward, stop. To get the robot to turn left, someone might say "go left", or "go to the left" or "move left" or any of hundreds of other permutations offered by the English language. 

So rather than hardcoding all those permutations, why not use Google Voice to convert the speech to text, and ApacheNLP to interpretat the speaker's intent.

# How It Works

Basically the Android app uses Google Voice to capture your speech and convert it to text. The text is then passed to Apache NLP to "interpret" what was said - it uses a text classification algorithm in conjunction with a training dataset (which you provide - see Providing A Training Dataset below). 

Once ApacheNLP has "interpreted" what was said, the interpretation is displayed on screen, along with the actual text that Google Voice thinks you said. Of course, rather than displaying the interpretation on screen, you could instead map it to some command to be sent to your robot.

# Providing A Training Dataset

The textclassification/trainer.txt file contains the commands you want to train the Android app to "understand".

The first word of each line is the command. The rest of the text on that line is a way to express that command in natural language.

For example, if you were wanting the Android app to understand directions, you might have a trainer.txt something like
 LEFT Please turn left
 LEFT Turn left
 LEFT go left
 LEFT Turn to the left
 LEFT go left now
 LEFT turn left now
 RIGHT Please turn right
 RIGHT Turn right
 ...

The more text "samples" you have in the trainer.txt file, the better that ApacheNLP can classify it correctly. 

The theory is that with enough samples, when someone says "can you pretty please turn to the left mate?", ApacheNLP will have the smarts to determine the most likely command is "LEFT", even though none of the samples were expressed that exact way.

Importantly, the commands - i.e. the first word of each line - must match exactly the enum in the CommandHelper class.

The trainer.txt file needs to be converted to a binary (.bin) format using Apache OpenNLP - the textclassification/train.sh script does this on a linux computer - though I was sloppy and hardcoded some file and directory names, which you will need to tweak for your environment. 

Anyway, once you have the .bin formatted training file, you dump it in the app/src/main/res/raw directory, where it will be picked up by the Android app and used to interpret what was said.
