//
// EvhCommunityServiceDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityServiceDTO
//
@interface EvhCommunityServiceDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSString* itemLabel;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSString* actionType;

@property(nonatomic, copy) NSString* actionData;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

