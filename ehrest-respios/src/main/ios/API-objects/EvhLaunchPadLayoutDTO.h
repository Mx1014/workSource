//
// EvhLaunchPadLayoutDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutDTO
//
@interface EvhLaunchPadLayoutDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* layoutJson;

@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

