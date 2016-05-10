//
// EvhAclinkDTO.h
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

