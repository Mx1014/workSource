//
// EvhApplyParkCardList.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhApplyParkCardDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyParkCardList
//
@interface EvhApplyParkCardList
    : NSObject<EvhJsonSerializable>


// item type EvhApplyParkCardDTO*
@property(nonatomic, strong) NSMutableArray* applyCard;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

