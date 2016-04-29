//
// EvhCreateContactByPhoneCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateContactByPhoneCommand
//
@interface EvhCreateContactByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

