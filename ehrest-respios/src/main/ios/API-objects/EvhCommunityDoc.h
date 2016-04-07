//
// EvhCommunityDoc.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityDoc
//
@interface EvhCommunityDoc
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* regionId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

