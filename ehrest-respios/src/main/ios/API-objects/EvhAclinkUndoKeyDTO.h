//
// EvhAclinkUndoKeyDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUndoKeyDTO
//
@interface EvhAclinkUndoKeyDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* keyId;

@property(nonatomic, copy) NSNumber* createTimeMs;

@property(nonatomic, copy) NSNumber* expireTimeMs;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

