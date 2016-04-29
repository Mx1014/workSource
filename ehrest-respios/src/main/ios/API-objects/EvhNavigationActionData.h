//
// EvhNavigationActionData.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNavigationActionData
//
@interface EvhNavigationActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* layoutName;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* callPhones;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSString* entityTag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

