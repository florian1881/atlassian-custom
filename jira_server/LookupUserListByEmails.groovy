import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserManager
import com.atlassian.jira.security.groups.GroupManager
import com.atlassian.crowd.embedded.api.Group
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger
import com.atlassian.jira.bc.user.search.UserSearchParams
import com.atlassian.jira.bc.user.search.UserSearchService

Logger  logit = Logger.getLogger("com.domain1.eu.logging")

// **********************************************
String[] userList = [
"cnxtagging.gmc@concentrix.com",
"1ee@duotone.io",
"402@duotone.io",
"Junmin.lee@leoburnett.co.kr",
"WW_singapore@wisewires.com",
"WW_vietnam@wisewires.com",
"amisha.doshi@razorfish.com",
"arrrn@duotone.io",
"auv@duotone.io",
"bcjeon@artience.com",
"bmchoi@wisewires.com",
"boeun@duotone.io",
"bomi.hwang@leoburnett.co.kr",
"bomi25.an@domain2.com",
"boram@duotone.io",
"byungchan.lee@welcomm.co.kr",
"carson.quiros@razorfish.com",
"celin.kim@domain2.com",
"cglee90@domain2.com",
"changhee.j@domain1pengtai.com",
"chirea.son@domain2.com ",
"chris.haines@razorfish.com",
"chris72.choi@domain2.com",
"chw.park@domain2.com",
"dabi@duotone.io",
"dabong@duotone.io",
"dahye109.kim@domain2.com",
"dain.han@leoburnett.co.kr",
"dajung3.jung@domain2.com ",
"dani.mariano@publicissapient.com",
"danielle.gianola@publicissapient.com",
"darye.lee@domain2.com",
"dasol.jung@razorfish.com",
"dave.kopec@razorfish.com",
"delphine.bachatene@digitas.com",
"demian@duotone.io",
"dh.mo@domain2.com",
"diep.tran@wisewires.com",
"dokyun1.kim@partner.sec.co.kr",
"donghee1.kim@domain2.com",
"dung.tran@wisewires.com",
"dykim@wisewires.com",
"dyst.choi@domain2.com",
"edith.koler@razorfish.com",
"elric.caponsacco@publicissapient.com",
"eojin.kim@domain2.com",
"eojin.son@publiciscommunications.com",
"ericahm.lee@domain2.com",
"eunjin.ong@partner.domain2.com",
"eunju20.lee@partner.domain2.com",
"eunsung.song@domain1pengtai.com",
"eunzoo.lee@domain2.com",
"evelyn.kim@domain2.com ",
"fave@duotone.io",
"gahyun11.kim@domain2.com",
"gg@duotone.io",
"giang.nguyen@wisewires.com",
"gini@duotone.io",
"gyujung.kim@domain2.com",
"gyujung.kim@domain2.com ",
"ha@duotone.io",
"hana@mocoplex.com",
"hannah.jung@domain2.com ",
"haze@duotone.io",
"heejung7.nam@domain2.com",
"heemoon.kim@domain2.com",
"hj613.jang@domain2.com",
"hjone.yang@domain2.com",
"hjtime.jeon@domain2.com",
"huong.le@wisewires.com",
"huong.nguyen2@wisewires.com",
"huong.tran2@wisewires.com",
"huyen.pham@wisewires.com",
"hyeran22.lim@partner.domain2.com",
"hyokeuny.lee@domain2.com",
"hyuna.jo@domain1pengtai.com",
"hyunsik.yim@domain2.com",
"iamkh.lee@domain2.com",
"ilhong@duotone.io",
"illy.cho@domain2.com",
"j0320.ha@domain2.com",
"jade1212.kim@domain2.com",
"jaebon.lim@domain2.com",
"james.king@publicissapient.com",
"james.king@razorfish.com",
"jason.bishop@razorfish.com",
"jb.oh@domain2.com",
"jen.han@domain2.com ",
"jeremy.grant@publicissapient.com",
"jessica.luszcz@publicissapient.com",
"jh9365.lee@domain2.com",
"jhchoi2@wisewires.com",
"jhoi.park@domain2.com",
"jieun1012.kim@domain2.com",
"jin.chung@domain2.com ",
"jina@mocoplex.com",
"jinsoo.yoo@domain2.com",
"jinu9.choi@partner.sec.co.kr",
"jinwon95.seo@domain2.com",
"jiyoun1.kim@domain2.com",
"jm0530.lee@domain2.com",
"jmfelix.shin@domain2.com",
"joey.castillo@publicissapient.com",
"jomy.jose@publicissapient.com",
"joon.ahn@publicissapient.com",
"joon2.kim@domain2.com",
"jskind.rho@domain2.com",
"jun@duotone.io",
"jungim.yoon@domain2.com",
"junhee.cho@leoburnett.co.kr",
"k.arul@domain1.com",
"katlyn.huang@razorfish.com",
"kcmo@duotone.io",
"kelly@duotone.io",
"key@duotone.io",
"khanh.tran@wisewires.com",
"kimmi@duotone.io",
"kris.gross@publicissapient.com",
"kwanghee2.yu@partner.domain2.com",
"kyunghoon10.kim@partner.sec.co.kr",
"kyungrok.son@partner.sec.co.kr",
"kyungsin.lee@domain2.com",
"lauren.shaffer@razorfish.com",
"liz.liu@razorfish.com",
"lomo@duotone.io",
"mai.do@wisewires.com",
"mamthaz.sinha@publicissapient.com",
"markmin.kim@domain2.com",
"michelle.lee@domain2.com",
"mike.reger@razorfish.com",
"mikej.kim@domain2.com",
"mink.choi@domain2.com",
"minseop.lee@domain2.com",
"mira.yi@domain2.com",
"miseo.park@domain2.com ",
"mjeong.park@domain2.com",
"moonjin.k@partner.domain2.com",
"ms87.park@domain2.com",
"my86.cho@domain2.com",
"nara@mocoplex.com",
"nathaniel.williams@publicissapient.com",
"nhkm@duotone.io",
"nikki.farrah@publicissapient.com",
"noah@duotone.io",
"p.kharel@domain2.com",
"purple@duotone.io",
"qyeong.kim@domain2.com",
"raju.dave@razorfish.com",
"rosa.park@domain1pengtai.com",
"ryan.harvey@razorfish.com",
"sam.padmore@digitas.com",
"samuel.stringerhye@razorfish.com",
"sanghun.jeong@domain2.com",
"sangil99.lee@partner.sec.co.kr",
"sarah11.park@domain1pengtai.com",
"sayhwan.kim@leoburnett.co.kr",
"sejeonga.kim@domain2.com",
"seola@duotone.io",
"seongjoong.jeon@domain2.com",
"sergio.reynoso@publicissapient.com",
"seungyub.lee@domain2.com",
"sh09.jung@domain2.com",
"sh@duotone.io",
"shin@duotone.io",
"shinhye1.kim@domain2.com",
"shyoh@artience.com",
"si75.park@domain2.com",
"sjpk@duotone.io",
"soda@duotone.io",
"sohyun.park1@domain2.com",
"sony.joy@domain2.com",
"sooji.lee@domain2.com",
"ssoabout.lee@domain2.com",
"stephanie.perdomo@publicissapient.com",
"steve@mocoplex.com",
"steven.wakabayashi@razorfish.com",
"sugil.yoon@domain2.com",
"suhee.kim@artience.com",
"suhyung.cha@domain2.com",
"sujin.joo@partner.sec.co.kr",
"suna@duotone.io",
"sungchol.cha@domain2.com",
"sungwoo.doh@domain2.com",
"sungwook.ryu@domain2.com",
"sunil04.kim@domain2.com",
"suyeon@mocoplex.com",
"sy0811.roh@domain2.com",
"tackhee.cho@publicis.co.kr",
"tan@duotone.io",
"tigerex@domain2.com",
"touj@duotone.io",
"trushin.lee@domain2.com",
"ts0706.kim@domain2.com ",
"victoria.berzin@publicissapient.com",
"william.cho@domain2.com",
"woo@duotone.io",
"yanghwa@duotone.io",
"yel@duotone.io",
"yelee.kwak@domain2.com ",
"yeonsoo1.kim@partner.sec.co.kr",
"yeri.chai@domain2.com",
"yhchoi@artience.com",
"yj07.kim@domain2.com",
"ykyung.kwon@domain2.com",
"yogesh.k5@partner.domain2.com",
"yohan.chun@publicis.co.kr",
"yongku.kim@domain2.com",
"yoojin1.song@domain2.com ",
"yoonmi.bae@domain2.com",
"young@duotone.io",
"yub@duotone.io",
"yun.jegal@domain2.com",
"yuna7.kim@domain2.com",
"yuna90.lee@partner.domain2.com",
"che@duotone.io",
"tim.wolf@razorfish.com",
"yeani.shin@domain2.com"
]
// **********************************************


//ApplicationUser user
def userUtil = ComponentAccessor.getUserUtil() 
UserManager userManager = ComponentAccessor.getUserManager()
GroupManager groupManager = ComponentAccessor.getGroupManager()
UserSearchService userSearchService = ComponentAccessor.getComponent(UserSearchService)
int usercount = 0
int domain1count = 0
int domain2count = 0
int othercount = 0
int newusercount = 0
int newdomain1count = 0
int newdomain2count = 0
int newothercount = 0
//def users 
// for each user to process
userList.each() {
   String name = (String)it
    if (name.endsWith("@domain1.com")) { 
        ++domain1count
        } else if (name.endsWith("@domain2.com")) { ++domain2count}
    	else {++othercount}
    
    	def users = userSearchService.findUsersByEmail(it)
    	if (users.size()== 0) {
         	logit.info("[*P6]," + it + ", not found,")
       
        if (name.endsWith("@domain1.com")) { ++newdomain1count
                                     }
    	else if (name.endsWith("@domain2.com")) { ++newdomain2count}
            
    	else {++newothercount}
            
 //           ++usercount
   //     	++newdomain1count
	//		++newdomain2count
		//	++newothercount 
    	} else {
                users.each(){user ->
    //user = userManager.getUserByName((String)useremail)
 
		String[] groups = userUtil.getGroupNamesForUser(user.getName())
    logit.info("[*P6]," + user + "," + (user.active?"active":"inactive") + ',"' + groups + '"')

                }         }
}
  logit.info("[*P6],new users ," + usercount)
  logit.info("[*P6],domain1 users ," + domain1count)
  logit.info("[*P6],new domain1 users ," + newdomain1count)
logit.info("[*P6],domain2 users ," + domain2count)
logit.info("[*P6],new domain2 users ," + newdomain2count)
logit.info("[*P6],other users ," + othercount)
logit.info("[*P6],new other users ," + newothercount)

