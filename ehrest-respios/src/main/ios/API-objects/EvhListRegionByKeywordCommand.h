//
// EvhListRegionByKeywordCommand.h
// generated at 2016-04-19 14:25:56 
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

