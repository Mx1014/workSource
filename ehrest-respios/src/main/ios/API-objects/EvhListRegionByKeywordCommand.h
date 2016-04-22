//
// EvhListRegionByKeywordCommand.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRegionByKeywordCommand
//
@interface EvhListRegionByKeywordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSNumber* scope;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* sortBy;

@property(nonatomic, copy) NSNumber* sortOrder;

@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

