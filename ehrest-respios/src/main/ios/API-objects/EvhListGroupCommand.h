//
// EvhListGroupCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupCommand
//
@interface EvhListGroupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* keyWord;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

