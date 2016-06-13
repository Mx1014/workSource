//
// EvhGetBannersByOrgCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBannersByOrgCommand
//
@interface EvhGetBannersByOrgCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* bannerLocation;

@property(nonatomic, copy) NSString* bannerGroup;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* sceneType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

