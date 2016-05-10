//
// EvhMarkerZoneActionData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMarkerZoneActionData
//
@interface EvhMarkerZoneActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

