//
// EvhOrganizationPostDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationPostDTO
//
@interface EvhOrganizationPostDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* orgId;

@property(nonatomic, copy) NSString* orgName;

@property(nonatomic, copy) NSNumber* postType;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* createTime;

@property(nonatomic, copy) NSString* orgType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

