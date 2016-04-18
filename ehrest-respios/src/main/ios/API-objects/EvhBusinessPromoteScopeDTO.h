//
// EvhBusinessPromoteScopeDTO.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessPromoteScopeDTO
//
@interface EvhBusinessPromoteScopeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSString* regionName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

