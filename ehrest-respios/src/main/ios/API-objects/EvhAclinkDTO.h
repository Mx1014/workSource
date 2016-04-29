//
// EvhAclinkDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkDTO
//
@interface EvhAclinkDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* deviceName;

@property(nonatomic, copy) NSString* firwareVer;

@property(nonatomic, copy) NSNumber* driver;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* manufacturer;

@property(nonatomic, copy) NSNumber* doorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

