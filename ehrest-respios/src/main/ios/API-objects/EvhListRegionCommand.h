//
// EvhListRegionCommand.h
// generated at 2016-03-25 09:26:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRegionCommand
//
@interface EvhListRegionCommand
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

