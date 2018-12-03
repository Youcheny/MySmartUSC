```diff
-if you want a clear readme, please visit
https://github.com/Youcheny/MySmartUSC
```

## Run Program
1. Click LOGIN TO EMAIL
2. Click SIGN IN
3. The program automatically jump to keywords modification page and allows you to imput keywords.
4. Cilck "show this list" everytime after you sign in. 
5. There are seven lists
	* Title ignore list: if a word such as "survey" is in this list, all future emails with this title will be marked as read and no notification will be sent.
	* Title important list: all future emails contain important keywords in their titles will trigger notification
	* Title star list: all future emails, in which content contains keywords in this list will be marked as read
	* Content ignore list: all future emails, in which content contains keywords in this list will be marked as read
	* Content important list:all future emails, in which content contains important keywords will trigger notification
	* content star list: all future emails, in which content contains keywords in this list will be marked as star
	* Important addresses list: all future emails that comes from important email addresses will trigger notification
6. Enter a keyword in "Title important list"
```diff
7. Make sure you click "done" after you add a keyword
```
8. Send a email to the login email address with the keyword above. Wait for 5 seconds and a notification will be shown.


Improvements

Sprint 1

1. The keywords will show themselves automatically once they have been retrieved from the database instead of showing only after the SHOW LIST button has been clicked. The SHOW LIST button is also removed as it is no longer needed.

2. It was not intuitive for the user to wait for a second or two before the keywords are displayed on their screen - while the device is busy retrieving data from the database. A spinner is added while the database read and write takes place and disappears once database-related activity ceases.

3.After 3000 seconds MySmartUSC will redirect the user to the login page to ask the user to log in again so that the access token will not expire after 3600 seconds and the user can keep updated with his/her email preference in the app.

4.Introduced the click-to-open-Gmail feature. A user will be able to click on a notification and check important emails in the Gmail App. However, the user will not be redirected to the KeywordAddressModification page when clicking back button from Gmail App.

Sprint 2

1. Some bug fixes have been added to the spinner. Specifically, the spinner no longer displays when the user logs out and logs back in again to the same account (as the keywords are already retrieved), but will show again if logged in to a different account (as a different set of keywords need to be loaded). 

2. The Email List button is integrated into the KeywordsModificationPage so that the user can see the list of emails categorized by the keyword lists.

3. Added an email list that shows all the important emails. The user does not have to go to Gmail to check what the important email is about. Instead, they can directly find who the email is from, the subject and a summary of contents.
	a. Sign in to the USC email account
	b. After notification pops up
	c. Click EMAILS PAGE, a list of important emails will pop up (if you have entered important keywords in the keywords list and your newest 8 emails contain the important keywords in your mailbox)
	d. Currently, the email list will not update automatically when you are on the email list page. But it will update if you click back button and click EMAILS PAGE again. An automated update is going to be implemented during the next sprint.

4. To use the shared keyword from other app feature, after logged in, the user can simply long click the keyword he/she wishes to add to the content important word list, and click share, then click MySmartUSC. After that, the keyword will be added to the content important word list automatically. The user can check the new keyword by selecting the list. 
	a. If the user did not log in before selecting the word, he/she will be redirected to the login page instead.
	
5. Complete the feature of redirecting to Gmail when notification is clicked. The user will now be redirected to Gmail when tapping a notification either showed up at top of the screen, in the drop-down list or collapsed in the drop-down list. The user can navigate back to the KeywordAddressModification page when navigating back from Gmail. Thus, the normal activity flow is maintained.


